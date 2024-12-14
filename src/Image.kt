import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO

fun generateColorImage(
    name: String,
    width: Int,
    height: Int,
    selector: (point: IntVector) -> Int
) {
    val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    (0 until height).forEach { y ->
        (0 until width).forEach { x ->
            img.setRGB(
                x,
                y,
                selector(x to y)
            )
        }
    }
    val outStream = FileOutputStream(File("images/$name.png"))
    ImageIO.write(img, "PNG", ImageIO.createImageOutputStream(outStream))
}

