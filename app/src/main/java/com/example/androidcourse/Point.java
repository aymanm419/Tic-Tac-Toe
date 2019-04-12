package com.example.androidcourse;

public class Point
{
    private int X = 0,Y = 0;
    public Point(int x,int y)
    {
        X = x;
        Y = y;
    }
    public Point(){}
    public void setX(int x)
    {
        this.X = x;
        return;
    }
    public void setY(int y)
    {
        this.Y = y;
        return;
    }
    public void setLocation(int x,int y)
    {
        X = x;
        Y = y;
        return;
    }
    public int getX()
    {
        return X;
    }
    public int getY()
    {
        return Y;
    }
}