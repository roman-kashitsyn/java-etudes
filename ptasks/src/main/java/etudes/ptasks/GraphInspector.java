package etudes.ptasks;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * Inspector for the tasks graph.
 * @author Roman Kashitsyn
 */
public class GraphInspector {
    LinkedList<Task> stack;
    IdentityHashMap<Task, Integer> taskIndexes;
    IdentityHashMap<Task, Integer> taskLowLinks;
    List<Set<Task>> stronglyConnectedComponents = Lists.newArrayList();
    int index;

    /**
     * Uses adopted Tarjan algorithm to determine cycles.
     * @param task task to check
     */
    public void checkForCycles(Task task) {
        index = 0;
        stack = Lists.newLinkedList();
        taskIndexes = new IdentityHashMap<Task, Integer>();
        taskLowLinks = new IdentityHashMap<Task, Integer>();

        strongConnect(task);

        for (Set<Task> connectedTasks : stronglyConnectedComponents) {
            if (connectedTasks.size() > 1 || hasSelfReference(connectedTasks)) {
                throw new CyclicDependenciesException(connectedTasks);
            }
        }
    }

    private void strongConnect(Task t) {
        taskIndexes.put(t, index);
        taskLowLinks.put(t, index);
        index += 1;
        stack.push(t);

        for (Task dep : t.getDependencies()) {
            if (!taskIndexes.containsKey(dep)) {
                strongConnect(dep);
                int dLowLink = taskLowLinks.get(dep);
                int tIndex = taskIndexes.get(t);
                taskLowLinks.put(t, Math.min(dLowLink, tIndex));
            } else if (stack.contains(dep)) {
                int tLowLink = taskLowLinks.get(t);
                int dIndex = taskIndexes.get(dep);
                taskLowLinks.put(t, Math.min(tLowLink, dIndex));
            }
        }
        if (taskLowLinks.get(t).equals(taskIndexes.get(t))) {
            Set<Task> newComponent = Sets.newHashSet();
            Task w;
            do {
                w = stack.pop();
                newComponent.add(w);
            } while (w != t);
            stronglyConnectedComponents.add(newComponent);
        }
    }

    private boolean hasSelfReference(Set<Task> singleItemComponent) {
        Preconditions.checkArgument(singleItemComponent.size() == 1);
        Task item = singleItemComponent.iterator().next();
        return item.getDependencies().contains(item);
    }
}
