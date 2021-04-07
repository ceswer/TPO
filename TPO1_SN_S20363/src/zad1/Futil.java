/*
 *
 *  @author Sukhetskyi Nazarii S20363
 *
 */

package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;

public class Futil {
    public static void processDir(String dirName, String resultFileName) {
        Charset cp1250 = Charset.forName("Cp1250");
        Charset utf_8 = StandardCharsets.UTF_8;
        List<Path> pathsList = new ArrayList<>();
        try (Stream<Path> pathsStream = Files.walk(Paths.get(dirName))) {
            pathsList = pathsStream.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException ignored) {}
        try (FileChannel fileChannel = FileChannel.open(Paths.get(resultFileName), CREATE, TRUNCATE_EXISTING, WRITE)) {
            for (Path path : pathsList) {
                FileChannel channel = FileChannel.open(path);
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int) channel.size());
                channel.read(byteBuffer);
                byteBuffer.flip();
                CharBuffer charBuffer = cp1250.decode(byteBuffer);
                fileChannel.write(utf_8.encode(charBuffer));
                channel.close();
            }
        } catch (IOException ignored) {}
    }
}
