
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Button;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

/**
 *
 * @author Sara Selim
 */
class MyCircle extends MyShape {

    int r;
    boolean fill;

    public MyCircle() {
    }

    public MyCircle(Point center, Color color, int r, boolean f) {
        super(center, color);
        this.r = r;
        this.fill = f;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getR() {
        return this.r;
    }

    public int calculateR(Point p) {
        r = (int) Math.sqrt((getStart().x - p.x) * (getStart().x - p.x) + (getStart().y - p.y) * (getStart().y - p.y));
        return r;
    }

    public boolean isFill() {
        return this.fill;
    }

    public void setFill(boolean f) {
        this.fill = f;
    }
}

class MyLine extends MyShape {

    Point end;

    public MyLine() {
    }

    public MyLine(Point start, Point end, Color color) {
        super(start, color);
        this.end = end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public Point getEnd() {
        return end;
    }
}

class MyRectangle extends MyShape {

    int width;
    int hight;
    boolean fill;

    public MyRectangle() {
    }

    public MyRectangle(Point start, Color color, int hight, int width, boolean f) {
        super(start, color);
        this.width = width;
        this.hight = hight;
        this.fill = f;
    }

    public void setCorners(Point s, Point e) {
        int h = e.y - s.y;
        int w = e.x - s.x;
        if (h >= 0 && w >= 0) {
            this.start = s;
            this.hight = h;
            this.width = w;
        }
        else if (h < 0 && w < 0) {
            this.start = e;
            this.hight = -1 * h;
            this.width = -1 * w;
        }
        else if (h >= 0 && w < 0) {
            this.start = new Point(e.x,s.y);
            this.hight = h;
            this.width = -1 * w;
        }
        else if (h < 0 && w >= 0) {
            this.start = new Point(s.x,e.y);
            this.hight = -1 * h;
            this.width = w;
        }
    }

    public int getHight() {
        return hight;
    }

    public int getWidth() {
        return width;
    }

    public boolean isFill() {
        return this.fill;
    }

    public void setFill(boolean f) {
        this.fill = f;
    }
}

class MyShape {

    Point start;
    Color color;

    public MyShape() {
    }

    public MyShape(Point start, Color color) {
        this.start = start;
        this.color = color;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point getStart() {
        return start;
    }

    public Color getColor() {
        return color;
    }
}

public class PaintBrush extends Applet {

    Color CURRENT_COLOR;
    String CURRENT_SHAPE;
    boolean CURRENT_SHAPE_state;
    Point START_POINT;
    Point CURRENT_POINT;
    boolean DRAGGED;

    MyLine line;
    MyCircle circle;
    MyRectangle rectangle;

    Vector<MyLine> lines;
    Vector<MyCircle> circles;
    Vector<MyRectangle> rectangles;
    Vector<MyRectangle> rectangles_eraser;

    Image offscreen;
    Graphics bufferGraphics;
    Dimension dim;

    @Override
    public void init() {

        lines = new Vector<MyLine>();
        circles = new Vector<MyCircle>();
        rectangles = new Vector<MyRectangle>();
        rectangles_eraser = new Vector<MyRectangle>();

        CURRENT_SHAPE = "Line";
        CURRENT_COLOR = Color.RED;
        DRAGGED = false;
        CURRENT_SHAPE_state = false;

        Button btn_eraser = new Button("eraser");
        Button btn_freeHand = new Button("Free Hand");

        Button btn_color_black = new Button("Black");
        Button btn_color_blue = new Button("Blue");
        Button btn_color_red = new Button("Red");
        Button btn_color_green = new Button("Green");

        Button btn_shape_line = new Button("Line");
        Button btn_shape_rec = new Button("Rectangle");
        Button btn_shape_cir = new Button("Circle");
        Button btn_shape_rec_fill = new Button("Fill Rectangle");
        Button btn_shape_cir_fill = new Button("Fill Circle");

        btn_eraser.addActionListener((ActionEvent e) -> {
            CURRENT_SHAPE = "eraser";
        });

        btn_freeHand.addActionListener((ActionEvent e) -> {
            CURRENT_SHAPE = "feeHAnd";
        });
        btn_color_red.addActionListener((ActionEvent e) -> {
            CURRENT_COLOR = Color.RED;
        });
        btn_color_black.addActionListener((ActionEvent e) -> {
            CURRENT_COLOR = Color.BLACK;
        });
        btn_color_blue.addActionListener((ActionEvent e) -> {
            CURRENT_COLOR = Color.BLUE;
        });
        btn_color_green.addActionListener((ActionEvent e) -> {
            CURRENT_COLOR = Color.GREEN;
        });
        btn_shape_line.addActionListener((ActionEvent e) -> {
            CURRENT_SHAPE = "Line";
        });
        btn_shape_rec.addActionListener((ActionEvent e) -> {
            CURRENT_SHAPE = "Rectangle";
            CURRENT_SHAPE_state = false;
        });
        btn_shape_rec_fill.addActionListener((ActionEvent e) -> {
            CURRENT_SHAPE = "Rectangle";
            CURRENT_SHAPE_state = true;
        });
        btn_shape_cir.addActionListener((ActionEvent e) -> {
            CURRENT_SHAPE = "Circle";
            CURRENT_SHAPE_state = false;
        });
        btn_shape_cir_fill.addActionListener((ActionEvent e) -> {
            CURRENT_SHAPE = "Circle";
            CURRENT_SHAPE_state = true;
        });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                DRAGGED = false;
                START_POINT = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (DRAGGED == true) {
                    switch (CURRENT_SHAPE) {
                        case "Line":
                            lines.add(new MyLine(line.getStart(), line.getEnd(), line.getColor()));
                            break;
                        case "Circle":
                            circles.add(new MyCircle(circle.getStart(), circle.getColor(), circle.getR(), circle.isFill()));
                            break;
                        case "Rectangle":
                            rectangles.add(new MyRectangle(rectangle.getStart(), rectangle.getColor(), rectangle.getHight(), rectangle.getWidth(), rectangle.isFill()));
                            break;
                    }
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                DRAGGED = true;
                CURRENT_POINT = e.getPoint();
                if (CURRENT_SHAPE.equalsIgnoreCase("eraser")) {
                    rectangles_eraser.add(new MyRectangle(CURRENT_POINT, Color.WHITE, 10, 10, true));
                } else if (CURRENT_SHAPE.equalsIgnoreCase("feeHAnd")) {
                    circles.add(new MyCircle(CURRENT_POINT, CURRENT_COLOR, 5, true));
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        add(btn_color_red);
        add(btn_color_blue);
        add(btn_color_green);
        add(btn_color_black);
        add(btn_shape_line);
        add(btn_shape_rec);
        add(btn_shape_cir);
        add(btn_shape_rec_fill);
        add(btn_shape_cir_fill);
        add(btn_eraser);
        add(btn_freeHand);
    }

    @Override
    public void paint(Graphics g) {
        dim = getSize();
        offscreen = createImage(dim.width, dim.height);
        bufferGraphics = offscreen.getGraphics();
        bufferGraphics.clearRect(0, 0, dim.width, dim.width);

        for (int i = 0; i < rectangles.size(); i++) {
            MyRectangle r = rectangles.elementAt(i);
            bufferGraphics.setColor(r.getColor());
            if (r.isFill()) {
                bufferGraphics.fillRect(r.getStart().x, r.getStart().y, r.getWidth(), r.getHight());
            } else {
                bufferGraphics.drawRect(r.getStart().x, r.getStart().y, r.getWidth(), r.getHight());
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            MyLine l = lines.elementAt(i);
            bufferGraphics.setColor(l.getColor());
            bufferGraphics.drawLine(l.getStart().x, l.getStart().y, l.getEnd().x, l.getEnd().y);
        }

        for (int i = 0; i < circles.size(); i++) {
            MyCircle c = circles.elementAt(i);
            bufferGraphics.setColor(c.getColor());
            if (c.isFill()) {
                bufferGraphics.fillOval(c.getStart().x, c.getStart().y, c.getR(), c.getR());
            } else {
                bufferGraphics.drawOval(c.getStart().x, c.getStart().y, c.getR(), c.getR());
            }
        }

        for (int i = 0; i < rectangles_eraser.size(); i++) {
            MyRectangle r = rectangles_eraser.elementAt(i);
            bufferGraphics.clearRect(r.getStart().x - 5, r.getStart().y - 5, 10, 10);
        }

        bufferGraphics.setColor(CURRENT_COLOR);
        if (DRAGGED == true) {
            switch (CURRENT_SHAPE) {
                case "Line":
                    line = new MyLine(START_POINT, CURRENT_POINT, CURRENT_COLOR);
                    bufferGraphics.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y);
                    break;
                case "Circle":
                    circle = new MyCircle();
                    circle.setColor(CURRENT_COLOR);
                    circle.setStart(START_POINT);
                    circle.calculateR(CURRENT_POINT);
                    circle.setFill(CURRENT_SHAPE_state);
                    if (circle.isFill()) {
                        bufferGraphics.fillOval(circle.getStart().x, circle.getStart().y, circle.getR(), circle.getR());
                    } else {
                        bufferGraphics.drawOval(circle.getStart().x, circle.getStart().y, circle.getR(), circle.getR());
                    }
                    break;
                case "Rectangle":
                    rectangle = new MyRectangle();
                    rectangle.setColor(CURRENT_COLOR);
                    rectangle.setCorners(START_POINT,CURRENT_POINT);
                    rectangle.setFill(CURRENT_SHAPE_state);
                    if (CURRENT_SHAPE_state) {
                        bufferGraphics.fillRect(rectangle.getStart().x, rectangle.getStart().y, rectangle.getWidth(), rectangle.getHight());
                    } else {
                        
                        
                        System.out.println(rectangle.getStart().y);
                        
                        System.out.println(rectangle.getStart().x);
                        
                        System.out.println(rectangle.getWidth());
                        System.out.println( rectangle.getHight());
                        
                        
                        bufferGraphics.drawRect(rectangle.getStart().x, rectangle.getStart().y, rectangle.getWidth(), rectangle.getHight());
                    }
                    break;
            }
        }
        g.drawImage(offscreen, 0, 0, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

}
