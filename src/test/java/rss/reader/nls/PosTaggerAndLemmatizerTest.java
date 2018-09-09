package rss.reader.nls;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.junit.Test;

import junit.framework.TestCase;
import rss.reader.model.Word;

public class PosTaggerAndLemmatizerTest extends TestCase {
	private static final String DELPHIN_TEXT = "A mayor on the north-west coast of France has ordered a halt to swimming and diving on local beaches because of potential risks from a rutting bottlenose dolphin.\r\n"
			+ "For weeks the dolphin, nicknamed Zafar, has been entertaining children near the Breton city of Brest and swimming with bathers and kayakers.\r\n"
			+ "But officials are worried the dolphin is seeking close contact with swimmers.\r\n"
			+ "Mayor Roger Lars said a number of bathers had been scared.\r\n"
			+ "\"I issued this ban to protect people's security,\" said the mayor of Landévennec.\r\n"
			+ "The 3m (10ft) dolphin had initially delighted locals, swimming with water sports enthusiasts in Brest harbour, but it had since moved south to his village of 300 people.\r\n"
			+ "According to Le Telegramme website, the dolphin had not been aggressive but had rubbed up against boats and swimmers alike, even in shallow waters.\r\n"
			+ "Last month a swimmer had to be rescued by a boat when the dolphin stopped her returning to the shore, it reported.\r\n"
			+ "A kayaker said the dolphin had leapt over his head and a Spanish holidaymaker had complained after being \"confronted by the dolphin, which was anxious to interact\".\r\n"
			+ "The last straw appeared to come last week when the dolphin tossed a young woman bather in the air with its nose.\r\n"
			+ "The mayor said swimming and diving would be banned wherever the dolphin's presence was confirmed, as well as approaching within 50m of the animal.\r\n"
			+ "A specialist at a Brest aquarium said there was a risk that the dolphin could cause unintentional harm with its tail fin.\r\n"
			+ "However, Breton lawyer Erwan Le Cornec denounced the restrictions as over the top. \"How many accidents have there been with a dolphin in Finistère since our two species existed? None,\" he complained.";

	private static final String F1 = "Photos: Story of the F1 season Fernando Alonso's car was launched over the top of Charles Leclerc on the opening corner of the Belgian Grand Prix. Sebastian Vettel went on to win at Spa to cut Lewis Hamilton's lead at the top of the Driver Standings to 17 points. Hide Caption 1 of 28 Photos: Story of the F1 season Lewis Hamilton celebrates with the trophy on the podium after winning the Hungarian Grand Prix at the Hungaroring near Budapest to extend his title lead over Sebastian Vettel to 24 points. Hide Caption 2 of 28 Photos: Story of the F1 season Hamilton celebrates an extraordinary comeback win at the German Grand Prix to give him a 17-point championship lead as title rival Sebastian Vettel crashed out Hide Caption 3 of 28 Photos: Story of the F1 season Dutch driver Max Verstappen claims a dramatic victory at the home of Red Bull Racing. But how does that impact the Drivers' Championship? Hide Caption 4 of 28 Photos: Story of the F1 season Melbourne, Australia -- 'Lucky' Vettel wins season opener – Sebastian Vettel took full advantage of a bizarre incident involving both cars of the American-owned Haas team to claim the opening race of the 2018 Formula One season in Australia. Hide Caption 5 of 28 Photos: Story of the F1 season Drivers' title race after round 1 – Vettel -- 25 points Hamilton -- 18 points Raikonnen -- 15 points Hide Caption 6 of 28 Photos: Story of the F1 season Sakhir, Bahrain -- Ferrari win soured by injured mechanic – Vettel won for the second time in as many races at the Bahrain Grand Prix. But the Italian team's victory was overshadowed after one of its mechanics suffered a broken leg when he was hit by Kimi Raikkonen's car during a pit stop. Hide Caption 7 of 28 Photos: Story of the F1 season Drivers' title race after round 2 – Vettel -- 50 points Hamilton -- 33 points Bottas -- 22 points Hide Caption 8 of 28 Photos: Story of the F1 season Shanghai, China – An inspired Daniel Ricciardo claimed a remarkable and unexpected victory from sixth on the grid after a tactical masterstroke by his Red Bull team in Shanghai, with furious championship leader Vettel back in eighth place. Hide Caption 9 of 28 Photos: Story of the F1 season Drivers' title race after round 3 – Vettel -- 54 points Hamilton -- 45 points Bottas -- 40 points Hide Caption 10 of 28 Photos: Story of the F1 season Baku, Azerbaijan -- Red Bull drivers shockingly crash as Hamilton triumphs – Lewis Hamilton was the chief beneficiary of a late puncture suffered by his Mercedes teammate Valtteri Bottas as he clinched his first win of the season at April's action-packed Azerbaijan Grand Prix. Hide Caption 11 of 28 Photos: Story of the F1 season Drivers' title race after round 4 – Hamilton -- 70 points Vettel -- 66 points Raikkonen -- 48 points Hide Caption 12 of 28 Photos: Story of the F1 season Barcelona, Spain -- Lewis Hamilton leads Mercedes one-two – After his unlikely victory in Azerbaijan, it was a second straight win for Hamilton as he bids for a fifth world championship -- and it could not have been more comfortable. Hide Caption 13 of 28 Photos: Story of the F1 season Drivers' title race after round 5 – Hamilton -- 95 points Vettel -- 78 points Bottas -- 57 points Hide Caption 14 of 28 Photos: Story of the F1 season Monaco -- Redemption for Ricciardo – Ricciardo nursed his ailing Red Bull to a remarkable victory on the streets of Monte Carlo and with it made up for his 2016 heartbreak on the same circuit. Hide Caption 15 of 28 Photos: Story of the F1 season Drivers' title race after round 6 – Hamilton -- 110 points Vettel -- 96 points Ricciardo -- 72 points Hide Caption 16 of 28 Photos: Story of the F1 season Canada -- Vettel wins to take title initiative – Sebastian Vettel's 50th career victory saw him replace Lewis Hamilton at the top of the championship standings to cap an emotional day for the Ferrari team. Hide Caption 17 of 28 Photos: Story of the F1 season Drivers' title race after round 7 – Vettel -- 121 points Hamilton -- 120 points Bottas -- 86 points Hide Caption 18 of 28 Photos: Story of the F1 season France -- Hamilton back in the groove – Briton Lewis Hamilton won the first French Grand Prix since 2008. The Mercedes driver avoided the worst of a dramatic start that saw title rival Sebastian Vettel clip Valtteri Bottas. Both drivers sustained damage in the collision, forcing them to pit early them and fall to the back of the grid. Hide Caption 19 of 28 Photos: Story of the F1 season Drivers' title race after round 8 – Hamilton -- 145 points Vettel -- 131 points Ricciardo -- 96 points Hide Caption 20 of 28 Photos: Story of the F1 season Austria -- Verstappen wins after Mercedes meltdown – Red Bull's Max Verstappen won a dramatic Austrian Grand Prix as hitherto championship leader Lewis Hamilton and Mercedes teammate, Valtteri Bottas, were forced to retire. Hide Caption 21 of 28 Photos: Story of the F1 season Drivers' title race after round 9 – Vettel - 146 points Hamilton - 145 points Raikkonen - 101 points Hide Caption 22 of 28 Photos: Story of the F1 season Britain -- Vettel wins despite Hamilton fightback – Home favorite Lewis Hamilton was denied a sixth victory at the British Grand Prix as Ferrari's Sebastian Vettel took control of the championship at Silverstone Hide Caption 23 of 28 Photos: Story of the F1 season Drivers' title race after round 10 – Vettel - 171Hamilton - 163Raikkonen - 116 Hide Caption 24 of 28 Photos: Story of the F1 season Germany -- Advantage Hamilton as Vettel crashes out – Hamilton fought back from 14th on the grid to claim an astonishing victory as Vettel crashed out at Hockenheim. Hide Caption 25 of 28 Photos: Story of the F1 season Drivers' title race after round 11 – Hamilton - 188 Vettel - 171 Raikkonen - 131 Hide Caption 26 of 28 Photos: Story of the F1 season Hungary -- Hamilton extends title lead with 'beautiful' win – Hamilton went into F1's summer break with a season-high 24-point advantage in the title race over Vettel after winning at the Hungaroring. Hide Caption 27 of 28 Photos: Story of the F1 season Drivers' title race after round 11 – Hamilton -- 213 points Vettel -- 189 points Raikkonen -- 146 points Hide Caption 28 of 28 Story highlights Belgian GP crash sparks halo discussion Alonso's car bounced off Leclerc's halo (CNN)Formula One's \"halo\" device has divided opinion since its introduction this season, but the safety cage was Charles Leclerc's guardian angel in a potentially serious first-corner crash at the Belgian Grand Prix. The titanium structure built over the cockpit to protect drivers' heads repelled the flying car of Fernando Alonso after the Spaniard was launched into the air following a shunt from the Renault of Germany's Nico Hülkenberg. Follow @cnnsport WOW 👀 😮 🙈 💥#BelgianGP 🇧🇪 #F1 @Charles_Leclerc pic.twitter.com/GOy3Jfszhd — Formula 1 (@F1) August 27, 2018 After the incident, there was substantial visible damage to the Frenchman Leclerc's halo. \"I saw the replay and how good was the proof for the halo,\" said Alonso, who was in the majority of drivers who voted for its introduction. Read More \"We didn't need any proof but it is a good thing. \"The positive side is we are all three OK, especially Charles. I flew over his car and the halo was a good thing to have today.\" Lap 44/44: Just look at the damage 👀#BelgianGP 🇧🇪 #F1 pic.twitter.com/SGBRneyLJD — Formula 1 (@F1) August 26, 2018 READ: Vettel wins Belgian GP after avoiding huge opening corner crash READ: Tatiana Calderon -- Men 'always expect a bit less from a girl' Ironically Leclerc, who emerged unscathed, has been vocal in his disapproval of its introduction. \"Never been a fan of the halo,\" he tweeted after the race, \"but I have to say that I was very happy to have it over my head today!\" End of the race in the 1st corner. Frustrating. Never been a fan of the halo but I have to say that I was very happy to have it over my head today ! 📷: @f1gregoryheirman / @fotoformulak pic.twitter.com/QILqoVtjVh — Charles Leclerc (@Charles_Leclerc) August 26, 2018 Speaking to reporters just after the incident, Leclerc said: \"If today it has been useful or not, I don't know. I don't know what would have happened without it but in some cases it is definitely helpful.\" READ: Green light for safety 'Halo' prompts mixed response from fans READ: F1 in 2018 -- Faster tires, fewer engines ... and a halo JUST WATCHED Women in Formula One: A Circuit special Replay More Videos ... MUST WATCH Women in Formula One: A Circuit special 22:46 Many fans and famous F1 figures have derided the new safety development, mainly taking umbrage with its appearance. Martin Brundle, who competed in 158 races over a 13-year period from 1984 to 1996, has called the halo \"plain ugly,\" while three-time champion Niki Lauda said \"the halo destroys the DNA of a Formula One car.\" We can end the HALO discussion now. It will save lives! #thanksFIA https://t.co/9d7gg3t6iT — Nico Rosberg (@nico_rosberg) August 26, 2018 Nico Rosberg, the 2016 world champion, said after the race: \"We can end the HALO discussion now. It will save lives!\" Jean Todt, president of motorsport's governing body the FIA, was one of the leading figures behind the halo's introduction, tweeting after the incident: \"Safety first. That's why we introduced the halo.\" Safety first. That's why we introduced the #Halo @alo_oficial @Charles_Leclerc https://t.co/z3gmC8jCVu — Jean Todt (@JeanTodt) August 26, 2018 Former Ferrari driver Felipe Massa, who was left with a fractured skull at the Hungarian GP weekend in 2009 after being stuck by debris, said: \"After seeing this, we can say ''The Halo is beautiful\"!!!\"\r\n"
			+ "";

	private static final int NUM_TRIES = 30;
	
	public static String text = "Joe Smith was born in California. "
			+ "In 2017, he went to Paris, France in the summer. " + "His flight left at 3:00pm on July 10th, 2017. "
			+ "After eating some escargot for the first time, Joe said, \"That was delicious!\" "
			+ "He sent a postcard to his sister Jane Smith. "
			+ "After hearing about Joe's trip, Jane decided she might go to France one day.";
	
	@Test
	public void testLemmatizer() {

		var example = PosTaggerAndLemmatizer.getInstance();

		double total = 0;

		String lemmaText = "";
		
		for (int i = 0; i < NUM_TRIES; i++) {
			long start = System.nanoTime();
			var lemmas = example.lemmatize(DELPHIN_TEXT + " (This is the " + i + "th test)");


			Collections.sort(lemmas, new Comparator<Word>() {

				@Override
				public int compare(Word o1, Word o2) {
					
					return o2.getScore()-o1.getScore();
				}
			});
			lemmaText = lemmas
					.stream()
					//.filter(lemma->lemma.frequency>1)
					.map(Object::toString)
					.collect(Collectors.joining(" "));
			long end = System.nanoTime();
			double singleTime = (end - start) / 1000_000_000.0;

			System.out.println(" Lemmatization: " + singleTime + " sec");

		}

		System.out.println(lemmaText);
		System.out.println("Total: " + total);
		System.out.println("avg: " + total / NUM_TRIES);

		/*
		 * // create a document object CoreDocument document = new CoreDocument(text);
		 * // annnotate the document pipeline.annotate(document); // examples
		 * 
		 * // 10th token of the document CoreLabel token = document.tokens().get(10);
		 * System.out.println("Example: token"); System.out.println(token);
		 * System.out.println();
		 * 
		 * // text of the first sentence String sentenceText =
		 * document.sentences().get(0).text(); System.out.println("Example: sentence");
		 * System.out.println(sentenceText); System.out.println();
		 * 
		 * // second sentence CoreSentence sentence = document.sentences().get(1);
		 * 
		 * // list of the part-of-speech tags for the second sentence List<String>
		 * posTags = sentence.posTags(); System.out.println("Example: pos tags");
		 * System.out.println(posTags); System.out.println();
		 * 
		 * // list of the ner tags for the second sentence List<String> nerTags =
		 * sentence.nerTags(); System.out.println("Example: ner tags");
		 * System.out.println(nerTags); System.out.println();
		 * 
		 * // constituency parse for the second sentence Tree constituencyParse =
		 * sentence.constituencyParse();
		 * System.out.println("Example: constituency parse");
		 * System.out.println(constituencyParse); System.out.println();
		 * 
		 * // dependency parse for the second sentence SemanticGraph dependencyParse =
		 * sentence.dependencyParse(); System.out.println("Example: dependency parse");
		 * System.out.println(dependencyParse); System.out.println();
		 * 
		 * // kbp relations found in fifth sentence List<RelationTriple> relations =
		 * document.sentences().get(4).relations();
		 * System.out.println("Example: relation");
		 * System.out.println(relations.get(0)); System.out.println();
		 * 
		 * // entity mentions in the second sentence List<CoreEntityMention>
		 * entityMentions = sentence.entityMentions();
		 * System.out.println("Example: entity mentions");
		 * System.out.println(entityMentions); System.out.println();
		 * 
		 * // coreference between entity mentions CoreEntityMention
		 * originalEntityMention = document.sentences().get(3).entityMentions().get(1);
		 * System.out.println("Example: original entity mention");
		 * System.out.println(originalEntityMention);
		 * System.out.println("Example: canonical entity mention");
		 * System.out.println(originalEntityMention.canonicalEntityMention().get());
		 * System.out.println();
		 * 
		 * // get document wide coref info Map<Integer, CorefChain> corefChains =
		 * document.corefChains();
		 * System.out.println("Example: coref chains for document");
		 * System.out.println(corefChains); System.out.println();
		 * 
		 * // get quotes in document List<CoreQuote> quotes = document.quotes();
		 * CoreQuote quote = quotes.get(0); System.out.println("Example: quote");
		 * System.out.println(quote); System.out.println();
		 * 
		 * // original speaker of quote // note that quote.speaker() returns an Optional
		 * System.out.println("Example: original speaker of quote");
		 * System.out.println(quote.speaker().get()); System.out.println();
		 * 
		 * // canonical speaker of quote
		 * System.out.println("Example: canonical speaker of quote");
		 * System.out.println(quote.canonicalSpeaker().get()); System.out.println();
		 * 
		 */

	}

	
}
