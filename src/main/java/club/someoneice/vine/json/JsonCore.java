package club.someoneice.vine.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.commons.compress.utils.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class JsonCore {
    static Gson gson = new Gson();
    static File file = new File(System.getProperty("user.dir") + File.separator +  "config", "tksrwine.json");
    public static List<WineData> DATA_LIST = Lists.newArrayList();
    public static void readFromJson() {
        try {
            if (!file.getParentFile().isDirectory()) file.getParentFile().mkdirs();
            if (!file.isFile()) file.createNewFile();

            byte[] bytes = new byte[(int) file.length()];
            FileInputStream stream = new FileInputStream(file);
            stream.read(bytes);
            stream.close();

            DATA_LIST = gson.fromJson(new String(bytes), new TypeToken<List<WineData>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
