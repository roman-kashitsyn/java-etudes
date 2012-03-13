package etudes.wget;

import static etudes.wget.Util.checkArgument;

/**
 * Progress bar formatter.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class ProgressBar {
    
    private final int width;
    
    private ProgressBar(int width) {
        this.width = width;
    }

    String render(int percents) {
        checkArgument(percents >= 0, "Percents must not be negative");
        checkArgument(percents <= 100, "Percents must not be > 100");
        StringBuilder builder = new StringBuilder("[");
        int innerWidth = width - 2;
        double step = 100.0 / innerWidth;
        for (int i = 0; i < innerWidth; ++i) {
            builder.append(chooseSymbolToRender(i, step, percents));
        }
        return builder.append(']').toString();
    }

    private char chooseSymbolToRender(int i, double step, int limit) {
        double current = i * step;
        double next = current + step;
        if (current < limit && next < 100 && limit <= next) {
            return '>';
        } else if (current < limit) {
            return '=';
        } else {
            return ' ';
        }
    }

    public static ProgressBar ofWidth(int width) {
        checkArgument(width > 4, "Progress bar width must be > 4");
        checkArgument(width <= 80, "Progress bar width must be <= 80");
        return new ProgressBar(width);
    }
}
