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
            wk = ImageIO.read(getClass().getResource("/client/clientModel/asset/wk.png"));
            bk = ImageIO.read(getClass().getResource("/client/clientModel/asset/bk.png"));
            wr = ImageIO.read(getClass().getResource("/client/clientModel/asset/wr.png"));
            br = ImageIO.read(getClass().getResource("/client/clientModel/asset/br.png"));
            wq = ImageIO.read(getClass().getResource("/client/clientModel/asset/wq.png"));
            bq = ImageIO.read(getClass().getResource("/client/clientModel/asset/bq.png"));
            wb = ImageIO.read(getClass().getResource("/client/clientModel/asset/wb.png"));
            bb = ImageIO.read(getClass().getResource("/client/clientModel/asset/bb.png"));
            wn = ImageIO.read(getClass().getResource("/client/clientModel/asset/wn.png"));
            bn = ImageIO.read(getClass().getResource("/client/clientModel/asset/bn.png"));
            wp = ImageIO.read(getClass().getResource("/client/clientModel/asset/wp.png"));
            bp = ImageIO.read(getClass().getResource("/client/clientModel/asset/bp.png"));
        } catch (IOException e) {
        }
    }
}
