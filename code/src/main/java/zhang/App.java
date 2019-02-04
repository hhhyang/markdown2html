package zhang;

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
      //  System.out.println(html);
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
}
