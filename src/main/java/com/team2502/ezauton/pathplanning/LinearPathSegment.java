package com.team2502.ezauton.pathplanning;

import com.team2502.ezauton.trajectory.geometry.ImmutableVector;
import com.team2502.ezauton.utils.MathUtils;

/**
 * A mostly-implemented linear IPathSegment which contains all methods save getSpeed(...).
 */
public abstract class LinearPathSegment implements IPathSegment
{
    private final ImmutableVector from;
    private final ImmutableVector to;
    private final boolean finish;
    private final ImmutableVector differenceVec;
    private final boolean beginning;
    private final double distanceStart;
    private final double distanceEnd;
    private final ImmutableVector dPos;
    //    private final double maxSpeed;
    private double length;
//    private MotionProfile motionProfiles;

    /**
     * Create a LinearPathSegment
     *
     * @param from          Starting location
     * @param to            Ending location
     * @param finish        If this is the last path segment in the path
     * @param beginning     If this is the first path segment in the path
     * @param distanceStart How far along the path the starting location is (arclength)
     */
    protected LinearPathSegment(ImmutableVector from, ImmutableVector to, boolean finish, boolean beginning, double distanceStart)
    {
//        this.maxSpeed = maxSpeed;
        this.finish = finish;
        this.from = from;
        this.to = to;
        differenceVec = to.sub(from);
        this.length = this.from.dist(this.to);
        if(MathUtils.epsilonEquals(0, length))
        {
            throw new IllegalArgumentException("PathSegment length must be non-zero.");
        }
        this.beginning = beginning;
        this.distanceStart = distanceStart;
        this.distanceEnd = distanceStart + length;
        dPos = to.sub(from);
    }


    /**
     * Get the point on the line segment that is the closest to the robot
     *
     * @param robotPos The position of the robot
     * @return The point on the line segment that is the closest to the robot
     */
    @Override
    public ImmutableVector getClosestPoint(ImmutableVector robotPos)
    {
        return MathUtils.Geometry.getClosestPointLineSegments(from, to, robotPos);
    }

    /**
     * Calculate how far along the path a point on the linesegment is
     *
     * @param linePos The point on the line
     * @return How far it is along the line segment
     */
    @Override
    public double getAbsoluteDistance(ImmutableVector linePos)
    {
        if(to.equals(linePos))
        {
            return distanceEnd;
        }

        if(from.equals(linePos))
        {
            return distanceStart;
        }

        // The difference between from, truncating 0
        ImmutableVector dif = linePos.sub(from);
        for(int i = 0; i < dif.getElements().length; i++)
        {
            double element = dif.get(i);
            if(element != 0)
            {
                double proportion = element / dPos.get(i);
                return getAbsoluteDistanceStart() + proportion * length;
            }
        }
        throw new ArithmeticException("Somehow dif has a dimension of 0.");
    }

    /**
     * Convert absolute distance (dist. along the path) to relative distance (dist. along this segment)
     *
     * @param absoluteDistance The absolute distance along the path
     * @return The relative distance along this path segment
     */
    public double getRelativeDistance(double absoluteDistance)
    {
        return absoluteDistance - distanceStart;
    }

    @Override
    public abstract double getSpeed(double absoluteDistance);

    /**
     * Assert a point that is x distance along the whole path is in this path segment
     *
     * @param absoluteDistance How far the point is along the path
     * @throws IllegalArgumentException
     */
    private void checkDistance(double absoluteDistance)
    {
        if(!MathUtils.Algebra.bounded(distanceStart, absoluteDistance, distanceEnd))
        {
            throw new IllegalArgumentException("Must be within bounds");
        }
    }

    /**
     * Based on the relative distance of a point along this path segment, get the location of the point
     *
     * @param relativeDistance How far the point is along this path segment
     * @return The aboslute location of the point
     */
    @Override
    public ImmutableVector getPoint(double relativeDistance)
    {
        return dPos.mul(relativeDistance / length).add(from);
    }

    /**
     * Get the distance left squared
     *
     * @param point A point on this path segment
     * @return The distance left on the path segment, squared
     * @deprecated
     */
    @Deprecated
    public double getDistanceLeft2(ImmutableVector point)
    {
        return to.sub(point).mag2();
    }

    /**
     * @return How far along the entire path that the from point is
     */
    @Override
    public double getAbsoluteDistanceStart()
    {
        return distanceStart;
    }

    /**
     * @return How far along the entire path that the end point is
     */
    @Override
    public double getAbsoluteDistanceEnd()
    {
        return distanceEnd;
    }

    @Override
    public boolean isBeginning()
    {
        return beginning;
    }

    @Override
    public boolean isFinish()
    {
        return finish;
    }

    public ImmutableVector getFrom()
    {
        return from;
    }

    public ImmutableVector getTo()
    {
        return to;
    }

    public double getLength()
    {
        return length;
    }

    public ImmutableVector getdPos()
    {
        return dPos;
    }

    @Override
    public String toString()
    {
        return "PathSegment{" +
               "from=" + from +
               ", to=" + to +
               '}';
    }


}
