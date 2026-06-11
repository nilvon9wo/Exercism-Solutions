using System.Diagnostics.CodeAnalysis;
using static Prism;

[SuppressMessage("Design", "CA1050:Declare types in namespaces", Justification = "<Pending>")]
public static class Prism
{
    public readonly record struct LaserInfo(double X, double Y, double Angle);

    public readonly record struct PrismInfo(int Id, double X, double Y, double Angle);

    private const double AngleTolerance = 0.1;

    public static int[] FindSequence(LaserInfo laser, PrismInfo[] prisms) 
        => [.. TraceLaser(laser, prisms)];

    private static IEnumerable<int> TraceLaser(LaserInfo initLaser, PrismInfo[] prisms)
    {
        double currentX = initLaser.X;
        double currentY = initLaser.Y;
        double currentAngle = initLaser.Angle;
        while (true)
        {
            LaserInfo currentLaser = new(currentX, currentY, currentAngle);
            PrismInfo? nextPrism = FindNearestPrismOnRay(currentLaser, prisms);
            if (nextPrism is null)
            {
                yield break;
            }

            PrismInfo prism = nextPrism.Value;
            yield return prism.Id;

            currentX = prism.X;
            currentY = prism.Y;
            currentAngle += prism.Angle;
        }
    }

    private static PrismInfo? FindNearestPrismOnRay(LaserInfo laser, PrismInfo[] prisms) 
        => prisms
            .Select(prism => CreatePrismProjectionSelector(prism, laser))
            .Where(x => x.Valid && x.Prism is not null)
            .OrderBy(x => x.Distance)
            .Select(x => x.Prism)
            .FirstOrDefault();

    private static PrismRayIntersectionCandidate CreatePrismProjectionSelector(PrismInfo prism, LaserInfo laser)
    {
        double dx = prism.X - laser.X;
        double dy = prism.Y - laser.Y;
        return dx == 0.0 && dy == 0.0 
            ? PrismRayIntersectionCandidate.CreateInvalid(double.MaxValue) 
            : EvaluatePrismRayIntersection(prism, laser, dx, dy);
    }

    private static PrismRayIntersectionCandidate EvaluatePrismRayIntersection(
            PrismInfo prism, 
            LaserInfo context, 
            double dx, 
            double dy
        )
    {
        double prismAngle = RadiansToDegrees(Math.Atan2(dy, dx));
        double angleDifference = NormalizeAngle(prismAngle - context.Angle);
        bool isOnRay = Math.Abs(angleDifference) <= AngleTolerance;
        return new PrismRayIntersectionCandidate
        {
            Prism = isOnRay
                ? prism
                : null,
            Distance = isOnRay
                ? dx * dx + dy * dy
                : double.MaxValue,
            Valid = isOnRay
        };
    }

    private static double NormalizeAngle(double angle)
    {
        double normalized = angle % 360.0;
        return (normalized <= -180.0)
            ? normalized + 360.0
            : (normalized > 180.0)
                ? normalized - 360.0
                : normalized;   
    }

    private static double RadiansToDegrees(double radians) 
        => radians * 180.0 / Math.PI;
}

internal record struct PrismRayIntersectionCandidate(Prism.PrismInfo? Prism, double Distance, bool Valid)
{
    public static implicit operator (Prism.PrismInfo? Prism, double Distance, bool Valid)(PrismRayIntersectionCandidate value) 
        => (value.Prism, value.Distance, value.Valid);

    public static implicit operator PrismRayIntersectionCandidate((Prism.PrismInfo? Prism, double Distance, bool Valid) value) 
        => new(value.Prism, value.Distance, value.Valid);

    public static PrismRayIntersectionCandidate CreateInvalid(double Distance)
        => new(null, Distance, false);
}
