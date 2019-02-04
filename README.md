# Java实现markdown转html，并且生成TOC目录
项目代码在code文件夹中
***

使用第三方库：[flexmark-java](https://github.com/vsch/flexmark-java)

使用IDEA新建一个maven项目：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190204092149283.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poYW5ncGV0ZXJ4,size_16,color_FFFFFF,t_70)
在pom.xml中添加依赖：

`注意flexmark的最新版本号可以到GitHub上查找: https://github.com/vsch/flexmark-java`

```java
    <dependency>
      <groupId>com.vladsch.flexmark</groupId>
      <artifactId>flexmark-all</artifactId>
      <version>0.40.4</version>
    </dependency>
```
修改App.java代码如下：
```java

import com.vladsch.flexmark.ext.jekyll.tag.JekyllTagExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataSet;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

/**
 * Hello world!
 * @author <a href="https://github.com/zhang0peter">Zhang</a>
 */
public class App
    implements KeyListener {
    public static JTextArea inputArea = new JTextArea();
    public static JEditorPane htmlPane = new JEditorPane();
    public static JEditorPane htmlPane2 = new JEditorPane();
    public static JFrame frame;

    public App() {
        frame = new JFrame("MarkdownPad");
        frame.setSize(1000, 800);
        frame.setLocation(200, 100);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        inputArea.setLineWrap(true);
        inputArea.addKeyListener(this);

        HTMLEditorKit kit = new HTMLEditorKit();
        htmlPane.setEditorKit(kit);
        htmlPane.setEditable(false);

        HTMLEditorKit kit2 = new HTMLEditorKit();
        htmlPane2.setEditorKit(kit2);
        htmlPane2.setEditable(false);


        JPanel p1 = new JPanel();
        p1.setLayout(new GridLayout(1, 3));

        JScrollPane scrollPane1 = new JScrollPane(inputArea);
        JScrollPane scrollPane2 = new JScrollPane(htmlPane);
        JScrollPane scrollPane3 = new JScrollPane(htmlPane2);
        p1.add(scrollPane3);
        p1.add(scrollPane1);
        p1.add(scrollPane2);
        container.add(p1, BorderLayout.CENTER);

        frame.show();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
    }

    public static void refresh() {

        String temp = inputArea.getText();
        MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(
                    TablesExtension.create(),
                    JekyllTagExtension.create(),
                    TocExtension.create(),
                    SimTocExtension.create()
                )).set(
            TocExtension.LEVELS, 255).set(
            TocExtension.TITLE, "Table of Contents").set(
            TocExtension.DIV_CLASS, "toc");
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(temp);
        String html = renderer.render(document);
        System.out.println(html);
        htmlPane.setText(html);

        Node document2 = parser.parse("[TOC]\n" + temp);
        String html2 = renderer.render(document2);
        //  System.out.println(html);
        if (html2.indexOf("<div class=\"toc\">") == -1) {
            htmlPane2.setText("");
        } else {
            htmlPane2.setText(html2.substring(0, 6 + html2.indexOf("</div>")));
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        refresh();
    }
    public static void main(String args[]) {
        new App();
    }
```
运行效果如图：     
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190204142439499.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3poYW5ncGV0ZXJ4,size_16,color_FFFFFF,t_70)
中间是编辑区域，左边是TOC生成的标题列表，右边是markdwon的实时渲染。
***
完整的代码在GitHub上：[https://github.com/zhang0peter](https://github.com/zhang0peter/markdown2html)
