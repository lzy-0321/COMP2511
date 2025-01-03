package metrics.graphing;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.RenderingHints;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JPanel;

import metrics.Plot;

public class Panel extends JPanel {
    private static final long serialVersionUID = -6588061082489436970L;
    private int marg = 60;
    private int numberYDivisions = 10;
    private int pointWidth = 4;
    private Color gridColor = new Color(200, 200, 200, 200);

    private Plot plot;

    private DataStrategy strategy;

    public Panel(Plot plot) {
        this.plot = plot;
        this.strategy = new AllPointsStrategy();
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
    }

    public void setStrategy(DataStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintAxes(g);
        paintData(g);
    }

    private void paintAxes(Graphics2D g) {
        g.setColor(Color.BLACK);

        int width = getWidth();
        int height = getHeight();
        g.draw(new Line2D.Double(marg, marg, marg, height - marg));
        g.draw(new Line2D.Double(marg, height / 2, width - marg, height / 2));
    }

    private void paintData(Graphics2D g) {
        List<Double> emitterData = strategy.calculate(plot.getData());

        // TODO change data based on statistic

        double x = (double) (getWidth() - 2 * marg) / (emitterData.size() - 1);
        double scale = (double) (getHeight() - 2 * marg)
                / (Math.max(Math.abs(getMinValue(emitterData)), Math.abs(getMaxValue(emitterData))) * 2);
        paintGridLines(g, emitterData);
        g.setColor(Color.RED);

        // set points to the graph
        for (int i = 0; i < emitterData.size(); i++) {
            double x1 = marg + i * x;
            double y1 = getHeight() / 2 + emitterData.get(i) * scale;
            g.fill(new Ellipse2D.Double(x1 - pointWidth / 2, y1 - pointWidth / 2, pointWidth, pointWidth));
        }
    }

    private void paintGridLines(Graphics2D g, List<Double> emitterData) {
        g.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = marg;
            int x1 = pointWidth + marg;
            int y0 = getHeight() - ((i * (getHeight() - marg * 2)) / numberYDivisions + marg);
            int y1 = y0;
            if (emitterData.size() > 0) {
                g.setColor(gridColor);
                g.drawLine(marg + 1 + pointWidth, y0, getWidth() - marg, y1);
                g.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinValue(emitterData)
                        + (getMaxValue(emitterData) - getMinValue(emitterData)) * ((i * 1.0) / numberYDivisions))
                        * 100)) / 100.0 + "";
                FontMetrics metrics = g.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g.drawLine(x0, y0, x1, y1);
        }

        // for x axis
        for (int i = 0; i < emitterData.size(); i++) {
            if (emitterData.size() > 1) {
                int x0 = i * (getWidth() - marg * 2) / (emitterData.size() - 1) + marg;
                int x1 = x0;
                int y0 = getHeight() - marg;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((emitterData.size() / 20.0)) + 1)) == 0) {
                    g.setColor(gridColor);
                    g.drawLine(x0, getHeight() - marg - 1 - pointWidth, x1, marg);
                }
                g.drawLine(x0, y0, x1, y1);
            }
        }
    }

    private double getMinValue(List<Double> emitterData) {
        double minValue = Double.MAX_VALUE;
        for (Double value : emitterData) {
            minValue = Math.min(minValue, value);
        }
        return minValue;
    }

    private double getMaxValue(List<Double> emitterData) {
        double maxValue = Double.MIN_VALUE;
        for (Double value : emitterData) {
            maxValue = Math.max(maxValue, value);
        }
        return maxValue;
    }

}

interface DataStrategy {
    public static final int TIME_FRAME = 10;

    List<Double> calculate(List<Double> data);
}

class AllPointsStrategy implements DataStrategy {
    @Override
    public List<Double> calculate(List<Double> data) {
        return data;
    }
}

// Average is the value of Sum/SampleCount during the specified period.
class AverageStrategy implements DataStrategy {
    @Override
    public List<Double> calculate(List<Double> data) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i += TIME_FRAME) {
            double sum = 0;
            for (int j = i; j < Math.min(i + TIME_FRAME, data.size()); j++) {
                sum += data.get(j);
            }
            result.add(sum / Math.min(TIME_FRAME, data.size() - i));
        }
        return result;
    }
}

//Maximum is the highest value observed during the specified period.
class MaxStrategy implements DataStrategy {
    @Override
    public List<Double> calculate(List<Double> data) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i += TIME_FRAME) {
            double max = Double.NEGATIVE_INFINITY;
            for (int j = i; j < Math.min(i + TIME_FRAME, data.size()); j++) {
                max = Math.max(max, data.get(j));
            }
            result.add(max);
        }
        return result;
    }
}

//Sum is the sum of the values of the all data points collected during the period
class SumStrategy implements DataStrategy {
    @Override
    public List<Double> calculate(List<Double> data) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i += TIME_FRAME) {
            double sum = 0;
            for (int j = i; j < Math.min(i + TIME_FRAME, data.size()); j++) {
                sum += data.get(j);
            }
            result.add(sum);
        }
        return result;
    }
}
