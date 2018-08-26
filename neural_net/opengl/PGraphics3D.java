package neural_net.opengl;

import neural_net.core.PGraphics;
import neural_net.core.PShape;
import neural_net.core.PShapeOBJ;


public class PGraphics3D extends PGraphicsOpenGL {

  public PGraphics3D() {
    super();
  }


  //////////////////////////////////////////////////////////////

  // RENDERER SUPPORT QUERIES


  @Override
  public boolean is2D() {
    return false;
  }


  @Override
  public boolean is3D() {
    return true;
  }


  //////////////////////////////////////////////////////////////

  // PROJECTION


  @Override
  protected void defaultPerspective() {
    perspective();
  }


  //////////////////////////////////////////////////////////////

  // CAMERA


  @Override
  protected void defaultCamera() {
    camera();
  }


  //////////////////////////////////////////////////////////////

  // MATRIX MORE!


  @Override
  protected void begin2D() {
    pushProjection();
    ortho(-width/2f, width/2f, -height/2f, height/2f);
    pushMatrix();

    // Set camera for 2D rendering, it simply centers at (width/2, height/2)
    float centerX = width/2f;
    float centerY = height/2f;
    modelview.reset();
    modelview.translate(-centerX, -centerY);

    modelviewInv.set(modelview);
    modelviewInv.invert();

    camera.set(modelview);
    cameraInv.set(modelviewInv);

    updateProjmodelview();
  }


  @Override
  protected void end2D() {
    popMatrix();
    popProjection();
  }



  //////////////////////////////////////////////////////////////

  // SHAPE I/O


  static protected boolean isSupportedExtension(String extension) {
    return extension.equals("obj");
  }


  static protected PShape loadShapeImpl(PGraphics pg, String filename,
                                                      String extension) {
    PShapeOBJ obj = null;

    if (extension.equals("obj")) {
      obj = new PShapeOBJ(pg.parent, filename);
      int prevTextureMode = pg.textureMode;
      pg.textureMode = NORMAL;
      PShapeOpenGL p3d = PShapeOpenGL.createShape((PGraphicsOpenGL)pg, obj);
      pg.textureMode = prevTextureMode;
      return p3d;
    }
    return null;
  }



  //////////////////////////////////////////////////////////////

  // SHAPE CREATION


//  @Override
//  protected PShape createShapeFamily(int type) {
//    PShape shape = new PShapeOpenGL(this, type);
//    shape.set3D(true);
//    return shape;
//  }
//
//
//  @Override
//  protected PShape createShapePrimitive(int kind, float... p) {
//    PShape shape = new PShapeOpenGL(this, kind, p);
//    shape.set3D(true);
//    return shape;
//  }


  /*
  @Override
  public PShape createShape(PShape source) {
    return PShapeOpenGL.createShape3D(this, source);
  }


  @Override
  public PShape createShape() {
    return createShape(PShape.GEOMETRY);
  }


  @Override
  public PShape createShape(int type) {
    return createShapeImpl(this, type);
  }


  @Override
  public PShape createShape(int kind, float... p) {
    return createShapeImpl(this, kind, p);
  }


  static protected PShapeOpenGL createShapeImpl(PGraphicsOpenGL pg, int type) {
    PShapeOpenGL shape = null;
    if (type == PConstants.GROUP) {
      shape = new PShapeOpenGL(pg, PConstants.GROUP);
    } else if (type == PShape.PATH) {
      shape = new PShapeOpenGL(pg, PShape.PATH);
    } else if (type == PShape.GEOMETRY) {
      shape = new PShapeOpenGL(pg, PShape.GEOMETRY);
    }
    shape.set3D(true);
    return shape;
  }


  static protected PShapeOpenGL createShapeImpl(PGraphicsOpenGL pg,
                                                int kind, float... p) {
    PShapeOpenGL shape = null;
    int len = p.length;

    if (kind == POINT) {
      if (len != 2 && len != 3) {
        showWarning("Wrong number of parameters");
        return null;
      }
      shape = new PShapeOpenGL(pg, PShape.PRIMITIVE);
      shape.setKind(POINT);
    } else if (kind == LINE) {
      if (len != 4 && len != 6) {
        showWarning("Wrong number of parameters");
        return null;
      }
      shape = new PShapeOpenGL(pg, PShape.PRIMITIVE);
      shape.setKind(LINE);
    } else if (kind == TRIANGLE) {
      if (len != 6) {
        showWarning("Wrong number of parameters");
        return null;
      }
      shape = new PShapeOpenGL(pg, PShape.PRIMITIVE);
      shape.setKind(TRIANGLE);
    } else if (kind == QUAD) {
      if (len != 8) {
        showWarning("Wrong number of parameters");
        return null;
      }
      shape = new PShapeOpenGL(pg, PShape.PRIMITIVE);
      shape.setKind(QUAD);
    } else if (kind == RECT) {
      if (len != 4 && len != 5 && len != 8 && len != 9) {
        showWarning("Wrong number of parameters");
        return null;
      }
      shape = new PShapeOpenGL(pg, PShape.PRIMITIVE);
      shape.setKind(RECT);
    } else if (kind == ELLIPSE) {
      if (len != 4 && len != 5) {
        showWarning("Wrong number of parameters");
        return null;
      }
      shape = new PShapeOpenGL(pg, PShape.PRIMITIVE);
      shape.setKind(ELLIPSE);
    } else if (kind == ARC) {
      if (len != 6 && len != 7) {
        showWarning("Wrong number of parameters");
        return null;
      }
      shape = new PShapeOpenGL(pg, PShape.PRIMITIVE);
      shape.setKind(ARC);

    } else if (kind == BOX) {
      if (len != 1 && len != 3) {
        showWarning("Wrong number of parameters");
        return null;
      }
      shape = new PShapeOpenGL(pg, PShape.PRIMITIVE);
      shape.setKind(BOX);
    } else if (kind == SPHERE) {
      if (len < 1 || 3 < len) {
        showWarning("Wrong number of parameters");
        return null;
      }
      shape = new PShapeOpenGL(pg, PShape.PRIMITIVE);
      shape.setKind(SPHERE);
    } else {
      showWarning("Unrecognized primitive type");
    }

    if (shape != null) {
      shape.setParams(p);
    }

    shape.set3D(true);
    return shape;
  }
  */
}