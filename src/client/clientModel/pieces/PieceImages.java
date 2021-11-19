/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.clientModel.pieces;

/**
 *
 * @author nxulu
 */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PieceImages {

    static Color WHITECOLOR = Color.WHITE;
    static Color BLACKCOLOR = Color.BLACK;
    static String PAWN = "♟";
    static String ROOK = "♜";
    static String KNIGHT = "♞";
    static String BISHOP = "♝";
    static String QUEEN = "♛";
    static String KING = "♚";

    static BufferedImage wk;
    static BufferedImage bk;
    static BufferedImage wr;
    static BufferedImage br;
    static BufferedImage wq;
    static BufferedImage bq;
    static BufferedImage wb;
    static BufferedImage bb;
    static BufferedImage wn;
    static BufferedImage bn;
    static BufferedImage wp;
    static BufferedImage bp;

    public PieceImages() {
        try {
            wk = ImageIO.read(new File(new File("src/client/clientModel/asset/wk.png").getCanonicalPath()));
            bk = ImageIO.read(new File(new File("src/client/clientModel/asset/bk.png").getCanonicalPath()));
            wr = ImageIO.read(new File(new File("src/client/clientModel/asset/wr.png").getCanonicalPath()));
            br = ImageIO.read(new File(new File("src/client/clientModel/asset/br.png").getCanonicalPath()));
            wq = ImageIO.read(new File(new File("src/client/clientModel/asset/wq.png").getCanonicalPath()));
            bq = ImageIO.read(new File(new File("src/client/clientModel/asset/bq.png").getCanonicalPath()));
            wb = ImageIO.read(new File(new File("src/client/clientModel/asset/wb.png").getCanonicalPath()));
            bb = ImageIO.read(new File(new File("src/client/clientModel/asset/bb.png").getCanonicalPath()));
            wn = ImageIO.read(new File(new File("src/client/clientModel/asset/wn.png").getCanonicalPath()));
            bn = ImageIO.read(new File(new File("src/client/clientModel/asset/bn.png").getCanonicalPath()));
            wp = ImageIO.read(new File(new File("src/client/clientModel/asset/wp.png").getCanonicalPath()));
            bp = ImageIO.read(new File(new File("src/client/clientModel/asset/bp.png").getCanonicalPath()));
        } catch (IOException e) {
        }
    }
}
