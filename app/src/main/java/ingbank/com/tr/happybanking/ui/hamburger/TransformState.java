package ingbank.com.tr.happybanking.ui.hamburger;


public class TransformState implements Cloneable {
    public int PosX;
    public int PosY;
    public float ScaleX;
    public float ScaleY;
    public int ScalePivotX;
    public int ScalePivotY;
    public float RotateDegrees;
    public int RotatePivotX;
    public int RotatePivotY;
    public int RColor;
    public int GColor;
    public int BColor;


    public TransformState() {
        init(0, 0, 1, 1, 0, 0, 0, 0, 0, 255, 102, 0);
    }

    @Override
    protected Object clone() {
        return new TransformState(this);
    }

    public TransformState(TransformState state) {
        this(state.PosX, state.PosY, state.ScaleX, state.ScaleY, state.ScalePivotX, state.ScalePivotY,
                state.RotateDegrees, state.RotatePivotX, state.RotatePivotY, state.RColor, state.GColor, state.BColor);
    }

    public TransformState(int posX, int posY, float scaleX, float scaleY, int scalePiX, int scalePiY,
                          float rotateDeg, int rotatePiX, int rotatePiY, int rColor, int gColor, int bColor) {
        init(posX, posY, scaleX, scaleY, scalePiX, scalePiY, rotateDeg, rotatePiX, rotatePiY, rColor, gColor, bColor);
    }

    private void init(int posX, int posY, float scaleX, float scaleY, int scalePiX, int scalePiY,
                      float rotateDeg, int rotatePiX, int rotatePiY, int rColor, int gColor, int bColor) {
        PosX = posX;
        PosY = posY;
        ScaleX = scaleX;
        ScaleY = scaleY;
        ScalePivotX = scalePiX;
        ScalePivotY = scalePiY;
        RotateDegrees = rotateDeg;
        RotatePivotX = rotatePiX;
        RotatePivotY = rotatePiY;
        RColor = rColor;
        GColor = gColor;
        BColor = bColor;
    }
}
