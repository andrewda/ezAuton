package org.github.ezauton.ezauton.recorder.base;

import javafx.animation.Interpolator;
import javafx.animation.KeyValue;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import org.github.ezauton.ezauton.trajectory.geometry.ImmutableVector;
import org.github.ezauton.ezauton.visualizer.IDataProcessor;
import org.github.ezauton.ezauton.visualizer.IEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotStateDataProcessor implements IDataProcessor
{
    private final RobotStateRecorder robotRec;
    private final List<RobotState> dataFrames;

    private Rectangle robot;
    private Circle posCircle;
    private Label headingLabel;
    private Label posLabel;
    private double spatialScaleFactorX;
    private double spatialScaleFactorY;
    private double originYPx;
    private double originXPx;
    private int robotLengthPx;
    private int robotWidthPx;

    public RobotStateDataProcessor(RobotStateRecorder robotRec)
    {
        this.robotRec = robotRec;
        dataFrames = robotRec.getDataFrames();
    }


    private double getX(double feetX)
    {
        return feetX * spatialScaleFactorX + originXPx;
    }

    private double getY(double feetY)
    {
        return -feetY * spatialScaleFactorY + originYPx;
    }

    @Override
    public void initEnvironment(IEnvironment environment)
    {
        this.spatialScaleFactorX = environment.getScaleFactorX();
        this.spatialScaleFactorY = environment.getScaleFactorY();

        this.originXPx = environment.getOrigin().get(0);
        this.originYPx = environment.getOrigin().get(1);

        // box for robot
        robotWidthPx = (int) (dataFrames.get(0).robotWidth * spatialScaleFactorX);
        robotLengthPx = (int) (dataFrames.get(0).robotLength * spatialScaleFactorY);
        robot = new Rectangle(0, 0, robotWidthPx, robotLengthPx);
        posCircle = new Circle(3, Paint.valueOf("white"));
        posCircle.setStroke(Paint.valueOf("black"));


        // heading info
        headingLabel = new Label("0 radians");

        // x y loc
        posLabel = new Label("(0, 0)");

        robot.setFill(Paint.valueOf("cyan"));
        robot.setStroke(Paint.valueOf("black"));
        environment.getFieldAnchorPane().getChildren().add(0, robot);
        environment.getFieldAnchorPane().getChildren().add(posCircle);

        environment.getDataGridPane(robotRec.getName()).addRow(0, new Label("Heading: "), headingLabel);
        environment.getDataGridPane(robotRec.getName()).addRow(1, new Label("Position: "), posLabel);
    }

    @Override
    public Map<Double, List<KeyValue>> generateKeyValues(Interpolator interpolator)
    {
        Map<Double, List<KeyValue>> ret = new HashMap<>();

        Rotate robotRotation = new Rotate();
        robot.getTransforms().add(robotRotation);

        for(RobotState frame : robotRec.getDataFrames())
        {
            List<KeyValue> keyValues = new ArrayList<>();
            double x, y, heading;

            if(frame.getPos() != null)
            {
                x = getX(frame.getPos().get(0));
                y = getY(frame.getPos().get(1));

                heading = frame.getHeading();

                double robotX = x - robotWidthPx / 2D;
                double robotY = y - robotLengthPx;

                keyValues.add(new KeyValue(robot.xProperty(), robotX, interpolator));
                keyValues.add(new KeyValue(robot.yProperty(), robotY, interpolator));
//                keyValues.add(new KeyValue(robot.rotateProperty(), -Math.toDegrees(heading), interpolator));
                keyValues.add(new KeyValue(robotRotation.angleProperty(), -Math.toDegrees(heading), interpolator));
                keyValues.add(new KeyValue(robotRotation.pivotXProperty(), x, interpolator));
                keyValues.add(new KeyValue(robotRotation.pivotYProperty(), y, interpolator));
                keyValues.add(new KeyValue(robot.visibleProperty(), true, interpolator));
                keyValues.add(new KeyValue(posCircle.centerXProperty(), x, interpolator));
                keyValues.add(new KeyValue(posCircle.centerYProperty(), y, interpolator));
                keyValues.add(new KeyValue(headingLabel.textProperty(), String.format("%.02f radians", heading)));
                keyValues.add(new KeyValue(posLabel.textProperty(), String.format("(%.02f, %.02f)", frame.getPos().get(0), frame.getPos().get(1))));
            }
            else
            {
                keyValues.add(new KeyValue(robot.visibleProperty(), false, interpolator));
            }

            ret.put(frame.getTime(),
                    keyValues);
        }

        return ret;
    }
}
