[1mdiff --git a/app/src/main/java/robustgametools/util/JsonFactory.java b/app/src/main/java/robustgametools/util/JsonFactory.java[m
[1mindex aed97fa..2c2dff5 100644[m
[1m--- a/app/src/main/java/robustgametools/util/JsonFactory.java[m
[1m+++ b/app/src/main/java/robustgametools/util/JsonFactory.java[m
[36m@@ -9,13 +9,12 @@[m [mpackage robustgametools.util;[m
  */[m
 public class JsonFactory {[m
 [m
[31m-    private static JsonFactory mJsonFactory = null;[m
[32m+[m[32m    private static JsonFactory mJsonFactory = new JsonFactory();[m
[32m+[m
[32m+[m[32m    private JsonFactory() {};[m
 [m
     // Singleton design pattern[m
     public static JsonFactory getInstance() {[m
[31m-        if (mJsonFactory == null) {[m
[31m-            mJsonFactory = new JsonFactory();[m
[31m-        }[m
         return mJsonFactory;[m
     }[m
 }[m
