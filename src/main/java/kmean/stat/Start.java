package kmean.stat;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Start {
    public static void main(String[] args) throws IOException {
		CSVReader reader = new CSVReader(new FileReader("G:/WukaiBag/Year2022/sustainableDevelopmentPaper/分区/Exp0817/聚类准备文件/中亚和南亚.csv"));
		FileWriter writer = new FileWriter("G:/WukaiBag/Year2022/sustainableDevelopmentPaper/分区/Exp0817/聚类准备文件/中亚和南亚.txt");

		List<String[]> myEntries = reader.readAll();
		List<Punto> puntos = new ArrayList<Punto>();
		for (String[] strings : myEntries) {
			//这里调整哪些参加聚类，哪些是属性值
			String[] Cluster = new String[]{strings[3], strings[4]};
			String[] Attribute = new String[]{strings[0],strings[1],strings[2]};
			Punto p = new Punto(Cluster, Attribute);
			puntos.add(p);
		}
		KMeans kmeans = new KMeans();
		for (int k = 1; k <= 5; k++) {
			KMeansResultado resultado = kmeans.calcular(puntos, k);
			writer.write("------- Con k=" + k + " ofv=" + resultado.getOfv()
					+ "-------\n");
			int i = 0;
			for (Cluster cluster : resultado.getClusters()) {
				i++;
				writer.write("-- Cluster " + i + " --\n");
				for (Punto punto : cluster.getPuntos()) {
					writer.write(punto.AttributeToString() + "," + punto.toString() + "\n");
				}
				writer.write("\n");
				writer.write("result,"+cluster.getCentroide().toString());
				writer.write("\n\n");
			}

		}
		writer.close();

	}
}