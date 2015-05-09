import java.io.*;
import java.util.*;

public class BuildIndexFile {

    private File file;
    private FileOutputStream fileOut;

    public BuildIndexFile(String path) {
        try {
            file = new File(path);
            file.createNewFile();
            fileOut = new FileOutputStream(file);
        }catch(IOException e) {}
    }

    private buildHtml() {

        String str = "<!DOCTYPE html>
        <html lang="de">
            <head>
                <meta charset="utf-8"/>
                <style>
                    body {
                        background-color: #eeeeee;
                        font-family: Verdana, Arial, sans-serif;
                        font-size: 90%;
                        margin: 4em 0;
                    }

                    article,
                    footer {
                        display: block;
                        margin: 0 auto;
                        width: 480px;
                    }

                    a {
                        color: #004466;
                        text-decoration: none;
                    }
                    a:hover {
                        text-decoration: underline;
                    }
                    a:visited {
                        color: #666666;
                    }

                    article {
                        background-color: #ffffff;
                        border: #cccccc solid 1px;
                        -moz-border-radius: 11px;
                        -webkit-border-radius: 11px;
                        border-radius: 11px;
                        padding: 0 1em;
                    }
                    h1 {
                        font-size: 140%;
                    }
                        ol {
                            line-height: 1.4em;
                            list-style-type: disc;
                        }
                    li.directory a:before {
                        content: '[ ';
                    }
                    li.directory a:after {
                        content: ' ]';
                    }

                    footer {
                        font-size: 70%;
                        text-align: center;
                    }
                </style>
                <title>Directory Index</title>
            </head>
            <body>
                <article>
                    <ol>
                        <li class="directory"><a href="/root">parent directory</a></li>
                        <li class="directory"><a href="files">files</a></li>
                        <li class="directory"><a href="images">images</a></li>
                        <li class="directory"><a href="videos">videos</a></li>
                        <li class="file"><a href="index.html">index.html</a></li>
                    </ol>
                </article>
            </body>
        </html>";
    }

    public File getFile() {
        return file;
    }
}
