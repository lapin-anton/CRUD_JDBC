import org.w3c.dom.Document;
import org.w3c.dom.Element;
import product.ProductType;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
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
            Element rootElement = doc.createElementNS("", "Products");
            rootElement.setAttribute("type", productType.name());
            doc.appendChild(rootElement);

            for (int i = 0; i < data.length; i++) {
                Element product = doc.createElement("Product");
                rootElement.appendChild(product);
                for (int j = 0; j < data[i].length; j++) {
                    Element param = doc.createElement("Param");
                    param.setAttribute("name", getParamName(columnNames[j]));
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
            case Constants.ID: name = "id";
                break;
            case Constants.MODEL: name = "model";
                break;
            case Constants.MAKER: name = "maker";
                break;
            case Constants.SPEED: name = "speed";
                break;
            case Constants.PRICE: name = "price";
                break;
            case Constants.HD: name = "hd";
                break;
            case Constants.RAM: name = "ram";
                break;
            case Constants.CD: name = "cd";
                break;
            case Constants.SCREEN: name = "screen";
                break;
            case Constants.PRINTING_TYPE: name = "printing_type";
                break;
            case Constants.COLOR: name = "color";
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
}
