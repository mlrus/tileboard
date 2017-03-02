package com.ipmetric.p1.client.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ipmetric.p1.client.Rack;

public class TestData {

	List<List<String>> testBoards;
	List<WordInfo> testWordInfo;
	List<String> testRacks;

	private static TestData testData = new TestData();

	public static TestData getTestData() {
		return testData;
	}

	public TestData() {
		testBoards = getBoardList();
		testRacks = getRackList();
		testWordInfo = new ArrayList<WordInfo>();

		testWordInfo.add(getScoredWords());
		final WordInfo miscWords = getRandomWords();
		while (testRacks.size() < testBoards.size()) {
			testRacks.add(Rack.unknownRack.getRack());
		}
		while (testWordInfo.size() < testBoards.size()) {
			testWordInfo.add(miscWords);
		}
	}

	public int numTests() {
		return testBoards.size();
	}

	public List<String> getBoard(final int i) {
		return testBoards.get(i);
	}

	public WordInfo getWordInfo(final int i) {
		return testWordInfo.get(i);
	}

	public String getRack(final int i) {
		return testRacks.get(i);
	}

	private WordInfo getRandomWords() {
		final WordInfo wordInfo = new WordInfo("Val", "Choice", "Cell");
		final List<String> testWords = Arrays.asList(new String[] { "ABASHEDLY", "ABASHES", "ABASHING", "ABASHMENT", "ABASHMENTS",
				"ABASIA", "ABASIAS", "ABASING", "ABATABLE", "ABATE", "ABATED", "ABATEMENT", "ABATEMENTS", "ABATER", "ABATERS",
				"ABATES", "ABATING", "ABATIS", "ABATISES", "ABATOR", "ABATORS", "ABATTIS", "ABATTISES", "ABATTOIR", "ABATTOIRS",
				"ABAXIAL", "ABAXILE", "ABAYA", "ABAYAS", "ABBA", "ABBACIES", "ABBACY", "ABBAS", "ABBATIAL", "ABBE", "ABBES",
				"ABBESS", "ABBESSES", "ABBEY", "ABBEYS", "ABBOT", });
		for (final String word : testWords) {
			wordInfo.addWord(word, "2", "Z99");
		}
		return wordInfo;
	}

	private WordInfo getScoredWords() {
		final WordInfo wordInfo = new WordInfo("Val", "Choice", "Cell");
		/*
		wordInfo.addWord("    2              AI 06J");
		wordInfo.addWord("    2              AN 06J");
		wordInfo.addWord("    2              AR 06J");
		wordInfo.addWord("    2              AS 06J");
		wordInfo.addWord("    2              EN 09J");
		wordInfo.addWord("    2              ER 09J");
		wordInfo.addWord("    2              ES 09J");
		wordInfo.addWord("    2              IN 03E");
		wordInfo.addWord("    2              IN 08N");
		wordInfo.addWord("    2              IS 03E");
		wordInfo.addWord("    2              IT K14");
		wordInfo.addWord("    2              LI 07J");
		wordInfo.addWord("    2              LO 07J");
		wordInfo.addWord("    2              NA 07N");
		wordInfo.addWord("    2              NA 14G");
		wordInfo.addWord("    2              NE L14");
		wordInfo.addWord("    2              NO J12");
		wordInfo.addWord("    2              NU 01G");
		wordInfo.addWord("    2              OE L14");
		wordInfo.addWord("    2              OI 03D");
		wordInfo.addWord("    2              OI 05H");
		wordInfo.addWord("    2              ON 05H");
		wordInfo.addWord("    2              ON 08N");
		wordInfo.addWord("    2              OR 04G");
		wordInfo.addWord("    2              OR 05H");
		wordInfo.addWord("    2              OS 05H");
		wordInfo.addWord("    2              RE L14");
		wordInfo.addWord("    2              SI 03D");
		wordInfo.addWord("    2              TI 13N");
		wordInfo.addWord("    2              TO 13N");
		wordInfo.addWord("    2              UN 01H");
		wordInfo.addWord("    2              US 01H");
		wordInfo.addWord("    3             AIN 06J");
		wordInfo.addWord("    3             AIR 06J");
		wordInfo.addWord("    3             AIS 06J");
		wordInfo.addWord("    3             ANI 06J");
		wordInfo.addWord("    3             ARS 06J");
		wordInfo.addWord("    3             ENS 09J");
		wordInfo.addWord("    3             EON 09J");
		wordInfo.addWord("    3             ERN 09J");
		wordInfo.addWord("    3             ERS 09J");
		wordInfo.addWord("    3              GO 14B");
		wordInfo.addWord("    3             INN 08M");
		wordInfo.addWord("    3             ION 08M");
		wordInfo.addWord("    3             LIN 07J");
		wordInfo.addWord("    3             LIS 07J");
		wordInfo.addWord("    3             NUS 01G");
		wordInfo.addWord("    3             NUT D06");
		wordInfo.addWord("    3             ORS 04G");
		wordInfo.addWord("    3             OUR 01G");
		wordInfo.addWord("    3             OUT D06");
		wordInfo.addWord("    3             RIN 03D");
		wordInfo.addWord("    3             RIN 04H");
		wordInfo.addWord("    3             RIN 08M");
		wordInfo.addWord("    3             RUN 01G");
		wordInfo.addWord("    3             RUT D06");
		wordInfo.addWord("    3             SEN 05D");
		wordInfo.addWord("    3             SIN 03D");
		wordInfo.addWord("    3             SIN 08M");
		wordInfo.addWord("    3             SIR 03D");
		wordInfo.addWord("    3             SON 08M");
		wordInfo.addWord("    3             SOU 01F");
		wordInfo.addWord("    3             SRI 04G");
		wordInfo.addWord("    3             SUN 01G");
		wordInfo.addWord("    3             UNS 01H");
		wordInfo.addWord("    3             URN 01H");
		wordInfo.addWord("    3             UTS D07");
		wordInfo.addWord("    4             ADO K11");
		wordInfo.addWord("    4             ADS K11");
		wordInfo.addWord("    4            AINS 06J");
		wordInfo.addWord("    4            AIRN 06J");
		wordInfo.addWord("    4            AIRS 06J");
		wordInfo.addWord("    4            ANIS 06J");
		wordInfo.addWord("    4              AS 14H");
		wordInfo.addWord("    4             GIN 14B");
		wordInfo.addWord("    4             GOR 14B");
		wordInfo.addWord("    4             GOS 14B");
		wordInfo.addWord("    4            IRON 04G");
		wordInfo.addWord("    4              IS J14");
		wordInfo.addWord("    4              IT 13M");
		wordInfo.addWord("    4              NO 13B");
		wordInfo.addWord("    4            NOUS 01F");
		wordInfo.addWord("    4            NUTS D06");
		wordInfo.addWord("    4            ONUS 01F");
		wordInfo.addWord("    4              OP 02G");
		wordInfo.addWord("    4             ORA 07M");
		wordInfo.addWord("    4              OS J14");
		wordInfo.addWord("    4            OURS 01G");
		wordInfo.addWord("    4            OUTS D06");
		wordInfo.addWord("    4              PI 02H");
		wordInfo.addWord("    4             RIA 07M");
		wordInfo.addWord("    4            RINS 04H");
		wordInfo.addWord("    4            RUIN 01G");
		wordInfo.addWord("    4            RUNS 01G");
		wordInfo.addWord("    4            RUTS D06");
		wordInfo.addWord("    4              SI I14");
		wordInfo.addWord("    4            SOUR 01F");
		wordInfo.addWord("    4            URNS 01H");
		wordInfo.addWord("    5             ARC 06J");
		wordInfo.addWord("    5              AY 06J");
		wordInfo.addWord("    5             CIS 03D");
		wordInfo.addWord("    5             CIS 10B");
		wordInfo.addWord("    5             CON 08M");
		wordInfo.addWord("    5             CON 10B");
		wordInfo.addWord("    5             COR 10B");
		wordInfo.addWord("    5             COS 10B");
		wordInfo.addWord("    5             CRU 01F");
		wordInfo.addWord("    5             CUR 01G");
		wordInfo.addWord("    5             CUT D06");
		wordInfo.addWord("    5            EONS 09J");
		wordInfo.addWord("    5            ERNS 09J");
		wordInfo.addWord("    5            EROS 09J");
		wordInfo.addWord("    5            GINS 14B");
		wordInfo.addWord("    5            GIRN 14B");
		wordInfo.addWord("    5            GIRO 14B");
		wordInfo.addWord("    5            GRIN 14B");
		wordInfo.addWord("    5              ID O14");
		wordInfo.addWord("    5            IRON 08L");
		wordInfo.addWord("    5           IRONS 04G");
		wordInfo.addWord("    5              LI 14N");
		wordInfo.addWord("    5            LINO 07J");
		wordInfo.addWord("    5            LINS 07J");
		wordInfo.addWord("    5            LION 07J");
		wordInfo.addWord("    5              LO 14N");
		wordInfo.addWord("    5            LOIN 07J");
		wordInfo.addWord("    5            LORN 07J");
		wordInfo.addWord("    5              OD O14");
		wordInfo.addWord("    5              OI C14");
		wordInfo.addWord("    5              ON C14");
		wordInfo.addWord("    5             OPS 02G");
		wordInfo.addWord("    5              OR C14");
		wordInfo.addWord("    5             ORA 14F");
		wordInfo.addWord("    5             ORC 04G");
		wordInfo.addWord("    5           ORNIS 04G");
		wordInfo.addWord("    5           ORRIS 04G");
		wordInfo.addWord("    5              OS C14");
		wordInfo.addWord("    5              OW A07");
		wordInfo.addWord("    5              OY 05H");
		wordInfo.addWord("    5             RAS 14G");
		wordInfo.addWord("    5             RIA 14F");
		wordInfo.addWord("    5           RISEN 05B");
		wordInfo.addWord("    5             ROC 04H");
		wordInfo.addWord("    5           RUINS 01G");
		wordInfo.addWord("    5           RUTIN D06");
		wordInfo.addWord("    5              SI 09D");
		wordInfo.addWord("    5             SIC 03D");
		wordInfo.addWord("    5           SIREN 05B");
		wordInfo.addWord("    5              SO 09D");
		wordInfo.addWord("    5            SORA 07L");
		wordInfo.addWord("    5            SORN 08L");
		wordInfo.addWord("    5              YA 07N");
		wordInfo.addWord("    5              YA 14G");
		wordInfo.addWord("    5              YE L14");
		wordInfo.addWord("    6             AHI 11A");
		wordInfo.addWord("    6             AHS 11A");
		wordInfo.addWord("    6             ANY 06J");
		wordInfo.addWord("    6            ARCO 06J");
		wordInfo.addWord("    6            ARCS 06J");
		wordInfo.addWord("    6            ASCI 06J");
		wordInfo.addWord("    6             AYS 06J");
		wordInfo.addWord("    6            CRIS 04G");
		wordInfo.addWord("    6            CRUS 01F");
		wordInfo.addWord("    6            CURN 01G");
		wordInfo.addWord("    6            CURS 01G");
		wordInfo.addWord("    6            CUTS D06");
		wordInfo.addWord("    6              EN I12");
		wordInfo.addWord("    6              EN 13H");
		wordInfo.addWord("    6              ER I12");
		wordInfo.addWord("    6              ER 13H");
		wordInfo.addWord("    6              ES I12");
		wordInfo.addWord("    6              ES 13H");
		wordInfo.addWord("    6             FIN E11");
		wordInfo.addWord("    6             FIR E11");
		wordInfo.addWord("    6             FON E11");
		wordInfo.addWord("    6             FOR E11");
		wordInfo.addWord("    6             FRO E11");
		wordInfo.addWord("    6              HI 03H");
		wordInfo.addWord("    6              HO 03H");
		wordInfo.addWord("    6              IN K05");
		wordInfo.addWord("    6              IN 14K");
		wordInfo.addWord("    6              IS K05");
		wordInfo.addWord("    6             ITS 13M");
		wordInfo.addWord("    6              NO K06");
		wordInfo.addWord("    6              NO N07");
		wordInfo.addWord("    6             NOR 13B");
		wordInfo.addWord("    6             NOS 13B");
		wordInfo.addWord("    6             NOW A06");
		wordInfo.addWord("    6             OCA 07M");
		wordInfo.addWord("    6              OH 03G");
		wordInfo.addWord("    6              OI K05");
		wordInfo.addWord("    6              ON K05");
		wordInfo.addWord("    6              OR K05");
		wordInfo.addWord("    6            ORCS 04G");
		wordInfo.addWord("    6              OS K05");
		wordInfo.addWord("    6            ROCS 04H");
		wordInfo.addWord("    6             ROW A06");
		wordInfo.addWord("    6              SH 03G");
		wordInfo.addWord("    6              SI K05");
		wordInfo.addWord("    6              SI K06");
		wordInfo.addWord("    6              SO K06");
		wordInfo.addWord("    6            SORA 14E");
		wordInfo.addWord("    6             SOW A06");
		wordInfo.addWord("    6             SRI 03C");
		wordInfo.addWord("    6             SYN 08M");
		wordInfo.addWord("    6            UNCI 01H");
		wordInfo.addWord("    6            UNCO 01H");
		wordInfo.addWord("    6            URIC 01H");
		wordInfo.addWord("    6             YEN 05D");
		wordInfo.addWord("    6             YIN 03D");
		wordInfo.addWord("    6             YIN 08M");
		wordInfo.addWord("    6             YON 08M");
		wordInfo.addWord("    6             YOU 01F");
		wordInfo.addWord("    7            AIRY 06J");
		wordInfo.addWord("    7            AYIN 06J");
		wordInfo.addWord("    7           CURIO 01G");
		wordInfo.addWord("    7           CURNS 01G");
		wordInfo.addWord("    7           CUTIN D06");
		wordInfo.addWord("    7           CUTIS D06");
		wordInfo.addWord("    7            FINO E11");
		wordInfo.addWord("    7            FINS E11");
		wordInfo.addWord("    7            FIRN E11");
		wordInfo.addWord("    7            FIRS E11");
		wordInfo.addWord("    7            FOIN E11");
		wordInfo.addWord("    7            FONS E11");
		wordInfo.addWord("    7             GOY 14B");
		wordInfo.addWord("    7             HIN 03H");
		wordInfo.addWord("    7             HIS 03H");
		wordInfo.addWord("    7             HON 03H");
		wordInfo.addWord("    7             HOS 03H");
		wordInfo.addWord("    7            ICON 08L");
		wordInfo.addWord("    7            LOCI 07J");
		wordInfo.addWord("    7             NIL 14L");
		wordInfo.addWord("    7           NORIA 14D");
		wordInfo.addWord("    7             OCA 14F");
		wordInfo.addWord("    7             OIL 14L");
		wordInfo.addWord("    7            ORCA 07L");
		wordInfo.addWord("    7           ORCIN 04G");
		wordInfo.addWord("    7              OW 06N");
		wordInfo.addWord("    7             PIN 02H");
		wordInfo.addWord("    7             PIS 02H");
		wordInfo.addWord("    7             POI 02H");
		wordInfo.addWord("    7             PRO 02H");
		wordInfo.addWord("    7             PSI 02H");
		wordInfo.addWord("    7            ROSY 04H");
		wordInfo.addWord("    7           RUNIC 01G");
		wordInfo.addWord("    7             RYA 07M");
		wordInfo.addWord("    7            SNOW A05");
		wordInfo.addWord("    7            SOCA 07L");
		wordInfo.addWord("    7            YOUR 01F");
		wordInfo.addWord("    7            YOUS 01F");
		wordInfo.addWord("    8             BYS L11");
		wordInfo.addWord("    8          COUSIN 01F");
		wordInfo.addWord("    8             COW A06");
		wordInfo.addWord("    8             COY 10B");
		wordInfo.addWord("    8             CRY 04G");
		wordInfo.addWord("    8             CRY 10B");
		wordInfo.addWord("    8           FINOS E11");
		wordInfo.addWord("    8           FIRNS E11");
		wordInfo.addWord("    8           FOINS E11");
		wordInfo.addWord("    8           FRONS E11");
		wordInfo.addWord("    8           GIRNS 14B");
		wordInfo.addWord("    8           GIRON 14B");
		wordInfo.addWord("    8           GIROS 14B");
		wordInfo.addWord("    8            GORY 14B");
		wordInfo.addWord("    8            GOYS 14B");
		wordInfo.addWord("    8           GRINS 14B");
		wordInfo.addWord("    8           GROIN 14B");
		wordInfo.addWord("    8            GYRI 14B");
		wordInfo.addWord("    8            GYRO 14B");
		wordInfo.addWord("    8            HINS 03H");
		wordInfo.addWord("    8            HISN 03H");
		wordInfo.addWord("    8            HONS 03H");
		wordInfo.addWord("    8            HORN 03H");
		wordInfo.addWord("    8             INS K04");
		wordInfo.addWord("    8             ION K04");
		wordInfo.addWord("    8            IRIS 03C");
		wordInfo.addWord("    8           IRONY 04G");
		wordInfo.addWord("    8            NISI 03B");
		wordInfo.addWord("    8              NO G01");
		wordInfo.addWord("    8            NOIR 03C");
		wordInfo.addWord("    8            NOIR 13B");
		wordInfo.addWord("    8             NOR K04");
		wordInfo.addWord("    8            NORI 03B");
		wordInfo.addWord("    8            NORI 13B");
		wordInfo.addWord("    8             NOS K04");
		wordInfo.addWord("    8             NOW 06M");
		wordInfo.addWord("    8           NUTSY D06");
		wordInfo.addWord("    8             OHS 03G");
		wordInfo.addWord("    8             ONS K04");
		wordInfo.addWord("    8            ORCA 14E");
		wordInfo.addWord("    8             ORS K04");
		wordInfo.addWord("    8              OS 13J");
		wordInfo.addWord("    8             OWN A07");
		wordInfo.addWord("    8              OY C14");
		wordInfo.addWord("    8            PINS 02H");
		wordInfo.addWord("    8            PION 02H");
		wordInfo.addWord("    8            PIRN 02H");
		wordInfo.addWord("    8            PISO 02H");
		wordInfo.addWord("    8            POIS 02H");
		wordInfo.addWord("    8            PONS 02H");
		wordInfo.addWord("    8            PORN 02H");
		wordInfo.addWord("    8            PROS 02H");
		wordInfo.addWord("    8             RHO 03G");
		wordInfo.addWord("    8            RIAS 14F");
		wordInfo.addWord("    8             RIN K04");
		wordInfo.addWord("    8             ROW 06M");
		wordInfo.addWord("    8             RYA 14F");
		wordInfo.addWord("    8              SI I01");
		wordInfo.addWord("    8             SIN K04");
		wordInfo.addWord("    8             SIR K04");
		wordInfo.addWord("    8            SOCA 14E");
		wordInfo.addWord("    8             SON K04");
		wordInfo.addWord("    8            SORI 03B");
		wordInfo.addWord("    8             SOW 06M");
		wordInfo.addWord("    8            SOYA 07L");
		wordInfo.addWord("    8            SPIN 02G");
		wordInfo.addWord("    8             SPY 02G");
		wordInfo.addWord("    8             SRI K04");
		wordInfo.addWord("    8           SYREN 05B");
		wordInfo.addWord("    8           UNCOS 01H");
		wordInfo.addWord("    8           YOURN 01F");
		wordInfo.addWord("    8           YOURS 01F");
		wordInfo.addWord("    9            CION 08L");
		wordInfo.addWord("    9            COIN 08L");
		wordInfo.addWord("    9            CONN 08L");
		wordInfo.addWord("    9           CORIA 14D");
		wordInfo.addWord("    9            CORN 08L");
		wordInfo.addWord("    9            CROW A05");
		wordInfo.addWord("    9          CURIOS 01G");
		wordInfo.addWord("    9            FICO E11");
		wordInfo.addWord("    9            FISC E11");
		wordInfo.addWord("    9            FOCI E11");
		wordInfo.addWord("    9             FOY E11");
		wordInfo.addWord("    9             FRY E11");
		wordInfo.addWord("    9             HIC 03H");
		wordInfo.addWord("    9           HORNS 03H");
		wordInfo.addWord("    9            INBY L09");
		wordInfo.addWord("    9          LORICA 07J");
		wordInfo.addWord("    9           OPSIN 02G");
		wordInfo.addWord("    9            ORBY L09");
		wordInfo.addWord("    9           PIONS 02H");
		wordInfo.addWord("    9           PIRNS 02H");
		wordInfo.addWord("    9           PORNS 02H");
		wordInfo.addWord("    9           PRION 02H");
		wordInfo.addWord("    9            RHOS 03G");
		wordInfo.addWord("    9            SCOW A05");
		wordInfo.addWord("    9            SCUT D05");
		wordInfo.addWord("    9            SHIN 03G");
		wordInfo.addWord("    9            SHRI 03G");
		wordInfo.addWord("    9            SNOW 06L");
		wordInfo.addWord("    9            SOWN A06");
		wordInfo.addWord("    9            SOYA 14E");
		wordInfo.addWord("    9             YOW A06");
		wordInfo.addWord("   10             COW 06M");
		wordInfo.addWord("   10           CRONY 04G");
		wordInfo.addWord("   10            FOYS E11");
		wordInfo.addWord("   10             HOY 03H");
		wordInfo.addWord("   10            IONS K03");
		wordInfo.addWord("   10            IRON K03");
		wordInfo.addWord("   10             NIB 04C");
		wordInfo.addWord("   10             NOB 04C");
		wordInfo.addWord("   10            NOIR K03");
		wordInfo.addWord("   10           NOIRS 13B");
		wordInfo.addWord("   10            NORI K03");
		wordInfo.addWord("   10          NORIAS 14D");
		wordInfo.addWord("   10           NORIS 03B");
		wordInfo.addWord("   10           NORIS 13B");
		wordInfo.addWord("   10            OCAS 14F");
		wordInfo.addWord("   10              OI 14J");
		wordInfo.addWord("   10             ORB 04C");
		wordInfo.addWord("   10           ORNIS 03B");
		wordInfo.addWord("   10          PRIONS 02H");
		wordInfo.addWord("   10          PRISON 02H");
		wordInfo.addWord("   10           RHINO 03G");
		wordInfo.addWord("   10             RIB 04C");
		wordInfo.addWord("   10            RINS K03");
		wordInfo.addWord("   10             ROB 04C");
		wordInfo.addWord("   10           ROSIN 03B");
		wordInfo.addWord("   10           ROSIN 04H");
		wordInfo.addWord("   10           SHORN 03G");
		wordInfo.addWord("   10             SIB 04C");
		wordInfo.addWord("   10             SOB 04C");
		wordInfo.addWord("   10            SORI K03");
		wordInfo.addWord("   10            SORN K03");
		wordInfo.addWord("   10          SPINOR 02G");
		wordInfo.addWord("   10             SRI K05");
		wordInfo.addWord("   10             XIS 12A");
		wordInfo.addWord("   11            CROW 06L");
		wordInfo.addWord("   11           FATES 11E");
		wordInfo.addWord("   11           GYRON 14B");
		wordInfo.addWord("   11           GYROS 14B");
		wordInfo.addWord("   11            HOYS 03H");
		wordInfo.addWord("   11            LINY 07J");
		wordInfo.addWord("   11            LORY 07J");
		wordInfo.addWord("   11              NO N05");
		wordInfo.addWord("   11           ORCAS 14E");
		wordInfo.addWord("   11             OSE I10");
		wordInfo.addWord("   11            PINY 02H");
		wordInfo.addWord("   11           PISCO 02H");
		wordInfo.addWord("   11            PONY 02H");
		wordInfo.addWord("   11            POSY 02H");
		wordInfo.addWord("   11            PYIN 02H");
		wordInfo.addWord("   11            PYRO 02H");
		wordInfo.addWord("   11          RHINOS 03G");
		wordInfo.addWord("   11            RYAS 14F");
		wordInfo.addWord("   11            SCOW 06L");
		wordInfo.addWord("   11              SO G03");
		wordInfo.addWord("   11              SO N05");
		wordInfo.addWord("   11             YOW 06M");
		wordInfo.addWord("   12             CHI 03G");
		wordInfo.addWord("   12             CIS K04");
		wordInfo.addWord("   12            COIN 03C");
		wordInfo.addWord("   12            COIR 03C");
		wordInfo.addWord("   12             CON K04");
		wordInfo.addWord("   12            CONI 03B");
		wordInfo.addWord("   12             COR K04");
		wordInfo.addWord("   12             COS K04");
		wordInfo.addWord("   12            CRIS 03C");
		wordInfo.addWord("   12           CROWN A05");
		wordInfo.addWord("   12           HORNY 03H");
		wordInfo.addWord("   12           HORSY 03H");
		wordInfo.addWord("   12            INCH 03E");
		wordInfo.addWord("   12            INRO K04");
		wordInfo.addWord("   12           IRONS K02");
		wordInfo.addWord("   12            NAOI 14G");
		wordInfo.addWord("   12           NOIRS K02");
		wordInfo.addWord("   12            NORI K04");
		wordInfo.addWord("   12           NORIS K02");
		wordInfo.addWord("   12           ORNIS K02");
		wordInfo.addWord("   12           PORNY 02H");
		wordInfo.addWord("   12           PROSY 02H");
		wordInfo.addWord("   12           PYINS 02H");
		wordInfo.addWord("   12           PYROS 02H");
		wordInfo.addWord("   12           ROSIN K02");
		wordInfo.addWord("   12            SNIB 04B");
		wordInfo.addWord("   12            SNOB 04B");
		wordInfo.addWord("   12            SORB 04B");
		wordInfo.addWord("   12            SORI K04");
		wordInfo.addWord("   12           SPINY 02G");
		wordInfo.addWord("   12           SPIRY 02G");
		wordInfo.addWord("   12             STY 13M");
		wordInfo.addWord("   12              YO K06");
		wordInfo.addWord("   12              YO N07");
		wordInfo.addWord("   13            CHIN 03G");
		wordInfo.addWord("   13            CHIS 03G");
		wordInfo.addWord("   13            CHON 03G");
		wordInfo.addWord("   13            INRO N03");
		wordInfo.addWord("   13              ON N06");
		wordInfo.addWord("   13              OS G02");
		wordInfo.addWord("   13             PIC 02H");
		wordInfo.addWord("   13            PYIC 02H");
		wordInfo.addWord("   13           SHINY 03G");
		wordInfo.addWord("   14           CHINO 03G");
		wordInfo.addWord("   14           CHINS 03G");
		wordInfo.addWord("   14           CHIRO 03G");
		wordInfo.addWord("   14           CHOIR 03G");
		wordInfo.addWord("   14            CIAO 14F");
		wordInfo.addWord("   14            CION K03");
		wordInfo.addWord("   14             COB 04C");
		wordInfo.addWord("   14            COIN K03");
		wordInfo.addWord("   14            COIR K03");
		wordInfo.addWord("   14            CONI K03");
		wordInfo.addWord("   14            CONS K03");
		wordInfo.addWord("   14            CORN K03");
		wordInfo.addWord("   14            CORS K03");
		wordInfo.addWord("   14            CRIS K03");
		wordInfo.addWord("   14            EROS 13H");
		wordInfo.addWord("   14            ICON K03");
		wordInfo.addWord("   14             ION N05");
		wordInfo.addWord("   14           IONIC 03B");
		wordInfo.addWord("   14            NOSY 13B");
		wordInfo.addWord("   14           ORCIN 03B");
		wordInfo.addWord("   14            ORCS K03");
		wordInfo.addWord("   14              OS 13K");
		wordInfo.addWord("   14            PICS 02H");
		wordInfo.addWord("   14           PRICY 02H");
		wordInfo.addWord("   14           PYRIC 02H");
		wordInfo.addWord("   14           RICIN 03B");
		wordInfo.addWord("   14            ROCS K03");
		wordInfo.addWord("   14        RUCHINGS B08");
		wordInfo.addWord("   14             SHY 03G");
		wordInfo.addWord("   14             SIT 13L");
		wordInfo.addWord("   14             SON N05");
		wordInfo.addWord("   14           SONIC 03B");
		wordInfo.addWord("   14             SOT 13L");
		wordInfo.addWord("   14            SPIC 02G");
		wordInfo.addWord("   14             SYN K04");
		wordInfo.addWord("   14           UNCOY 01H");
		wordInfo.addWord("   14             YIN K04");
		wordInfo.addWord("   14              YO N05");
		wordInfo.addWord("   14             YON K04");
		wordInfo.addWord("   14            YONI 03B");
		wordInfo.addWord("   15          CHINOS 03G");
		wordInfo.addWord("   15          CHIROS 03G");
		wordInfo.addWord("   15          CHOIRS 03G");
		wordInfo.addWord("   15           CORBY L08");
		wordInfo.addWord("   15           HYSON 03H");
		wordInfo.addWord("   15            IRON N04");
		wordInfo.addWord("   15              OY K05");
		wordInfo.addWord("   16           CIONS K02");
		wordInfo.addWord("   16           COINS K02");
		wordInfo.addWord("   16           COIRS K02");
		wordInfo.addWord("   16             CON N05");
		wordInfo.addWord("   16            CONI K04");
		wordInfo.addWord("   16           CORNS K02");
		wordInfo.addWord("   16            CRIB 04B");
		wordInfo.addWord("   16           ICONS K02");
		wordInfo.addWord("   16          IRONIC 03A");
		wordInfo.addWord("   16           NOISY 13B");
		wordInfo.addWord("   16             NOS G01");
		wordInfo.addWord("   16           ORCIN K02");
		wordInfo.addWord("   16          ORCINS 04G");
		wordInfo.addWord("   16             PRY 02H");
		wordInfo.addWord("   16             QIS C07");
		wordInfo.addWord("   16           SCION K02");
		wordInfo.addWord("   16           SCORN K02");
		wordInfo.addWord("   16              SI 15B");
		wordInfo.addWord("   16              SO 15B");
		wordInfo.addWord("   16              US 09B");
		wordInfo.addWord("   16            YINS K03");
		wordInfo.addWord("   16             YOB 04C");
		wordInfo.addWord("   16            YONI K03");
		wordInfo.addWord("   16           YONIS 03B");
		wordInfo.addWord("   17            CION N04");
		wordInfo.addWord("   17           GORSY 14B");
		wordInfo.addWord("   17            ICON N04");
		wordInfo.addWord("   17             IVY 02D");
		wordInfo.addWord("   17             SOY K04");
		wordInfo.addWord("   17            SPRY 02G");
		wordInfo.addWord("   17             YON N05");
		wordInfo.addWord("   18              IS C11");
		wordInfo.addWord("   18          ORCINS K01");
		wordInfo.addWord("   18          ROSINY 04H");
		wordInfo.addWord("   18           SCION N03");
		wordInfo.addWord("   18             SIN 15B");
		wordInfo.addWord("   18             SIR 15B");
		wordInfo.addWord("   18              SO C12");
		wordInfo.addWord("   18             SON 15B");
		wordInfo.addWord("   18           SPICY 02G");
		wordInfo.addWord("   18             SRI 15B");
		wordInfo.addWord("   18            YONI K04");
		wordInfo.addWord("   18           YONIS K02");
		wordInfo.addWord("   19            CONI N05");
		wordInfo.addWord("   19            NOSY K03");
		wordInfo.addWord("   19              OY N06");
		wordInfo.addWord("   19            ROSY K03");
		wordInfo.addWord("   19            SORI 15B");
		wordInfo.addWord("   19            SORN 15B");
		*/
		wordInfo.addWord("   20            COYS K03");
		wordInfo.addWord("   20              IS 15A");
		wordInfo.addWord("   20              OS 15A");
		wordInfo.addWord("   20             SOY N05");
		wordInfo.addWord("   20            YONI N05");
		wordInfo.addWord("   20           YONIC 03B");
		wordInfo.addWord("   21             COY K04");
		wordInfo.addWord("   21             CRY K04");
		wordInfo.addWord("   21             ICY K04");
		wordInfo.addWord("   21           IRONY K02");
		wordInfo.addWord("   21           NOISY K02");
		wordInfo.addWord("   21             SYN 15B");
		wordInfo.addWord("   22             COY N05");
		wordInfo.addWord("   22           SCION 15B");
		wordInfo.addWord("   22           SCORN 15B");
		wordInfo.addWord("   22             SIC 15B");
		wordInfo.addWord("   22           SONIC 15B");
		wordInfo.addWord("   23            CONY K03");
		wordInfo.addWord("   23            CORY K03");
		wordInfo.addWord("   23            COSY K03");
		wordInfo.addWord("   23          ROSINY K01");
		wordInfo.addWord("   23            SCRY K03");
		wordInfo.addWord("   24            SCRY 15B");
		wordInfo.addWord("   24             SOY 15B");
		wordInfo.addWord("   24            SYNC 15B");
		wordInfo.addWord("   25           CORNY K02");
		wordInfo.addWord("   25           CRONY K02");
		return wordInfo;
	}

	private List<String> getRackList() {
		final List<String> rackList = new ArrayList<String>();
		rackList.add("YSNRICO");
		rackList.add("UAGTFKT");
		rackList.add("???????");
		rackList.add("???????");
		rackList.add("???????");
		rackList.add("???????");
		return rackList;
	}

	private List<List<String>> getBoardList() {
		final List<List<String>> boardList = new ArrayList<List<String>>();
		final String[][] boards = new String[][] {
				new String[] { "_______U_______", "____V__P_______", "____I__H_______", "____B__R_______", "____EN_O_______",
						"_____ORE_A____W", "__QUOTA__L____A", "WRIT_AMIDO____N", "_U____J__E____I", "_C___ZEROS___ME",
						"AH__FATE__ABLER", "XI____SPENDY_T_", "_N_____E_____T_", "_G_____A_____L_", "_______LISTENED" },

				new String[] { "_______WADMAL__", "_____COO____OF_", "______WED___OR_", "____________NAB", "_____________PE",
						"_J____________N", "_ES________M__N", "PEERIE_ZA__I__I", "__E_____TRADUCE", "__DIARIST__G__S",
						"__L_HE_U___URB_", "_VIVA__N___T_O_", "RIN____L___SOX_", "HAG____I_____Y_", "O______T_______" },
				new String[] { "___TETRAMER____", "___O____IF__MHO", "_KNEW____TORII_", "___I__________P", "___N__GANJA___L",
						"__OGIVE__ODE_ZA", "______E_______C", "______SIX__L__I", "______E_UNPILED", "__________EM_Y_",
						"_________FAB_ET", "_________L_E_BO", "_________A_D_AT", "_________W___R_", "___________NOSH" },
				new String[] { "___TETRAMER____", "___O____IF__MHO", "_KNEW____TORII_", "___I__________P", "___N__GANJA___L",
						"__OGIVE__ODE_ZA", "______E_______C", "______SIX__L__I", "______E_UNPILED", "__________EM_Y_",
						"_________FAB_ET", "_________L_E_BO", "_________A_D_AT", "_________W___R_", "_______CRY_NOSH" },
				new String[] { "___TETRAMER____", "___O____IF__MHO", "_KNEW____TORII_", "___I__________P", "___N__GANJA___L",
						"__OGIVE__ODE_ZA", "______E_____VAC", "______SIX__L__I", "______E_UNPILED", "__________EM_Y_",
						"_________FAB_ET", "_________L_E_BO", "_________A_D_AT", "_________W___R_", "_______CRY_NOSH" } };

		for (final String[] board : boards) {
			boardList.add(Arrays.asList(board));
		}
		return boardList;
	}

}
