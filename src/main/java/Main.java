import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {
//        PDDocument pdf = new PDDocument();
//
//        for (int i = 0; i < 10; i++) {
//            PDPage blankPage = new PDPage();
//            pdf.addPage(blankPage);
//        }
//
//        pdf.save("C:\\Users\\tanay\\OneDrive\\Documents\\pdfHandle\\myPdf.pdf");
//        System.out.println("PDF created");
//        pdf.close();
        Scanner sc = new Scanner(System.in);
        boolean flag = true;
        PDFMergerUtility mPDF = new PDFMergerUtility();
        //taking the destination directory(with name of the merged pdf) from the user
        System.out.println("Set destination for the merged .pdf file");
        String destn = sc.nextLine();
        mPDF.setDestinationFileName("" + destn);

        do {
            System.out.println("Enter the path of the PDF or Image you want to merge else press E to exit");
            String path = sc.nextLine();

            if (path.charAt(0) == 'E') {
                flag = false;
            } else {
                String extn = path.substring(path.lastIndexOf('.'));
                System.out.println(extn);
                switch (extn) {
                    case ".pdf":
                        System.out.println("in case .pdf");
                        mPDF.addSource(new File("" + path));
                        break;
                    case ".png":
                    case ".jpeg":
                    case ".jpg":
                        //creating a temporary PDF for holding the image
                        System.out.println("in case images");
                        PDDocument tempPDF = new PDDocument();
                        tempPDF.addPage(new PDPage());
                        String tempPath =
                                path.substring(0, path.lastIndexOf("\\") + 1) + "" + System.currentTimeMillis() +
                                        ".pdf";
                        tempPDF.save("" + tempPath);
                        tempPDF.close();
                        //loading images in it
                        PDDocument doc = PDDocument.load(new File("" + tempPath));
                        PDPage page = doc.getPage(0);
                        PDImageXObject imageXObject = PDImageXObject.createFromFile(path + "", doc);
                        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
                        contentStream.drawImage(imageXObject, 70, 250);
                        contentStream.close();
                        doc.save("" + tempPath);
                        doc.close();
                        //adding it to the pdf where we are merging everything
                        mPDF.addSource("" + tempPath);
                        break;
                    default:
                        System.out.println("Invalid path or filetype!");
                }

            }
        } while (flag);

        mPDF.mergeDocuments();

    }
}
