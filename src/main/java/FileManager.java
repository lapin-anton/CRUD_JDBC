import org.w3c.dom.*;
import product.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    private static final String ID = "Id";
    private static final String MODEL = "Model";
    private static final String MAKER = "Maker";
    private static final String PRICE = "Price";
    private static final String SPEED = "Speed";
    private static final String HD = "Hd";
    private static final String RAM = "Ram";
    private static final String CD = "Cd";
    private static final String SCREEN = "Screen";
    private static final String PRINTING_TYPE = "PrintingType";
    private static final String COLOR = "Color";

    private File file;
    private FileOutputStream out;
    private FileInputStream in;

    private Object[][] data;
    private Object[] columnNames;
    private ProductType type;

    public static void save(File file, Object[][] data, Object[] columnNames, ProductType productType) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element rootElement = doc.createElement("Products");
            rootElement.setAttribute("type", productType.name());
            doc.appendChild(rootElement);

            for (int i = 0; i < data.length; i++) {
                Element product = doc.createElement("Product");
                rootElement.appendChild(product);
                for (int j = 0; j < data[i].length; j++) {
                    Element param = doc.createElement(getParamName(columnNames[j]));
                    param.appendChild(doc.createTextNode(String.valueOf(data[i][j])));
                    product.appendChild(param);
                }
            }
            writeDocument(doc, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getParamName(Object columnName) {
        String name = "";
        switch ((String) columnName) {
            case Constants.ID: name = ID;
                break;
            case Constants.MODEL: name = MODEL;
                break;
            case Constants.MAKER: name = MAKER;
                break;
            case Constants.SPEED: name = SPEED;
                break;
            case Constants.PRICE: name = PRICE;
                break;
            case Constants.HD: name = HD;
                break;
            case Constants.RAM: name = RAM;
                break;
            case Constants.CD: name = CD;
                break;
            case Constants.SCREEN: name = SCREEN;
                break;
            case Constants.PRINTING_TYPE: name = PRINTING_TYPE;
                break;
            case Constants.COLOR: name = COLOR;
        }
        return name;
    }

    private static void writeDocument(Document document, File file) throws TransformerFactoryConfigurationError {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            FileOutputStream fos = new FileOutputStream(file);
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
        } catch (TransformerException | IOException e) {
            e.printStackTrace(System.out);
        }
    }

    public static Result open(File file) {
        Result result = null;
        ProductType productType = null;
        ArrayList<Product> list = new ArrayList<>();
        try {
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            Document doc = documentBuilder.parse(file);
            Node root = doc.getDocumentElement();
            NamedNodeMap attribs = root.getAttributes();
            Node item = attribs.getNamedItem("type");
            String type = item.getNodeValue();
            NodeList products = doc.getElementsByTagName("Product");
            switch (type) {
                case "PC": productType = ProductType.PC;
                    list = fillPC(products);
                    break;
                case "LAPTOP": productType = ProductType.LAPTOP;
                    list = fillLaptop(products);
                    break;
                case "PRINTER": productType = ProductType.PRINTER;
                    list = fillPrinter(products);
                    break;
                default: productType = ProductType.NONE;
            }
        } catch (Exception ex) {
            ExceptionHandler.log(ex);
        }
        result = new Result(productType, list);
        return result;
    }

    private static ArrayList<Product> fillPrinter(NodeList nodes) {
        ArrayList<Product> list = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;
                int id = Integer.parseInt(getTagValue(ID, element));
                String model = getTagValue(MODEL, element);
                String maker = getTagValue(MAKER, element);
                int price = Integer.parseInt(getTagValue(PRICE, element));
                String type = getTagValue(PRINTING_TYPE, element);
                String color = getTagValue(COLOR, element);
                list.add(new Printer(id, model, maker, price, type, color));
            }
        }
        return list;
    }

    private static ArrayList<Product> fillLaptop(NodeList nodes) {
        ArrayList<Product> list = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;
                int id = Integer.parseInt(getTagValue(ID, element));
                String model = getTagValue(MODEL, element);
                String maker = getTagValue(MAKER, element);
                int price = Integer.parseInt(getTagValue(PRICE, element));
                int speed = Integer.parseInt(getTagValue(SPEED, element));
                int hd = Integer.parseInt(getTagValue(HD, element));
                int ram = Integer.parseInt(getTagValue(RAM, element));
                int screen = Integer.parseInt(getTagValue(SCREEN, element));
                list.add(new Laptop(id, model, maker, price, speed, hd, ram, screen));
            }
        }
        return list;
    }

    private static ArrayList<Product> fillPC(NodeList nodes) {
        ArrayList<Product> list = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;
                int id = Integer.parseInt(getTagValue(ID, element));
                String model = getTagValue(MODEL, element);
                String maker = getTagValue(MAKER, element);
                int price = Integer.parseInt(getTagValue(PRICE, element));
                int speed = Integer.parseInt(getTagValue(SPEED, element));
                int hd = Integer.parseInt(getTagValue(HD, element));
                int ram = Integer.parseInt(getTagValue(RAM, element));
                String cd = getTagValue(CD, element);
                list.add(new PC(id, model, maker, price, speed, hd, ram, cd));
            }
        }
        return list;
    }

    // получаем значение элемента по указанному тегу
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}
