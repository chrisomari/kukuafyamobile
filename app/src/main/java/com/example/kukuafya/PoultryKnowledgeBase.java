package com.example.kukuafya;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PoultryKnowledgeBase {

    private Map<String, String> breedInfo;
    private Map<String, String> housingInfo;
    private Map<String, String> feedingInfo;
    private Map<String, String> diseaseInfo;
    private Map<String, String> businessInfo;
    private Map<String, String> biosecurityInfo;
    private String[] farmingTips;
    private Random random;

    public PoultryKnowledgeBase() {
        initializeBreedInfo();
        initializeHousingInfo();
        initializeFeedingInfo();
        initializeDiseaseInfo();
        initializeBusinessInfo();
        initializeBiosecurityInfo();
        initializeFarmingTips();
        random = new Random();
    }

    private void initializeBreedInfo() {
        breedInfo = new HashMap<>();

        breedInfo.put("overview", "There are several chicken breeds suitable for Kenyan farmers, each with unique characteristics:\n\n" +
                "1. Local/Indigenous Breeds\n" +
                "2. Layers\n" +
                "3. Broilers\n" +
                "4. Dual-purpose Breeds\n\n" +
                "Which breed would you like to know more about?");

        breedInfo.put("local", "LOCAL/INDIGENOUS BREEDS:\n\n" +
                "Benefits:\n" +
                "• Highly adaptable to local conditions\n" +
                "• Disease resistant\n" +
                "• Low maintenance\n" +
                "• Can scavenge for food\n" +
                "• Good broodiness (natural hatching)\n\n" +
                "Challenges:\n" +
                "• Slower growth rate\n" +
                "• Lower egg production (80-150 eggs/year)\n" +
                "• Smaller body size\n\n" +
                "Best for: Small-scale farming with limited resources, free-range systems");

        breedInfo.put("layers", "LAYER BREEDS:\n\n" +
                "Popular varieties: White Leghorn, Rhode Island Red, Isa Brown\n\n" +
                "Benefits:\n" +
                "• High egg production (250-300 eggs/year)\n" +
                "• Efficient feed conversion\n" +
                "• Start laying at 18-20 weeks\n\n" +
                "Challenges:\n" +
                "• Less meat production\n" +
                "• More susceptible to diseases\n" +
                "• Require balanced commercial feed\n\n" +
                "Best for: Commercial egg production, battery cage or deep litter systems");

        breedInfo.put("broilers", "BROILER BREEDS:\n\n" +
                "Popular varieties: Cobb 500, Ross 308\n\n" +
                "Benefits:\n" +
                "• Rapid growth (ready for market in 6-8 weeks)\n" +
                "• Excellent feed conversion ratio\n" +
                "• High meat yield\n\n" +
                "Challenges:\n" +
                "• No egg production value\n" +
                "• Susceptible to heat stress\n" +
                "• Require high-protein feed\n" +
                "• Need good ventilation\n\n" +
                "Best for: Commercial meat production, intensive systems");

        breedInfo.put("dual", "DUAL-PURPOSE BREEDS:\n\n" +
                "Popular varieties: Kenbro, Kuroiler, Sasso, Rainbow Rooster\n\n" +
                "Benefits:\n" +
                "• Good egg production (200-250 eggs/year)\n" +
                "• Reasonable meat yield\n" +
                "• More adaptable than pure broilers/layers\n" +
                "• Better suited to semi-intensive systems\n\n" +
                "Challenges:\n" +
                "• Not as specialized as pure layers or broilers\n" +
                "• Moderate growth rate\n\n" +
                "Best for: Small to medium-scale farmers wanting both eggs and meat");
    }

    private void initializeHousingInfo() {
        housingInfo = new HashMap<>();

        housingInfo.put("overview", "POULTRY HOUSING BASICS:\n\n" +
                "Good poultry housing should provide:\n" +
                "• Protection from predators and harsh weather\n" +
                "• Proper ventilation without drafts\n" +
                "• Adequate space per bird\n" +
                "• Easy cleaning and maintenance\n" +
                "• Proper lighting\n\n" +
                "What specific aspect of housing would you like to know about?");

        housingInfo.put("site", "HOUSING SITE SELECTION:\n\n" +
                "• Choose well-drained ground\n" +
                "• Orient house east-west to minimize direct sunlight\n" +
                "• Ensure good air circulation\n" +
                "• Place away from residential areas (odor and noise)\n" +
                "• Consider access to water and electricity\n" +
                "• Allow space for future expansion\n" +
                "• Maintain distance from other poultry farms (biosecurity)");

        housingInfo.put("floor", "FLOORING OPTIONS:\n\n" +
                "1. Concrete Floor:\n" +
                "• Durable and easy to clean\n" +
                "• Good for biosecurity\n" +
                "• Higher initial cost\n" +
                "• Can be cold - needs litter material\n\n" +
                "2. Earthen Floor:\n" +
                "• Lower cost\n" +
                "• Natural drainage\n" +
                "• Harder to disinfect\n" +
                "• May harbor parasites\n\n" +
                "Litter Materials:\n" +
                "• Wood shavings (most common)\n" +
                "• Rice husks\n" +
                "• Sawdust (avoid for young chicks)\n" +
                "• Chopped straw\n\n" +
                "Litter should be 4-6 inches deep and kept dry");

        housingInfo.put("ventilation", "VENTILATION REQUIREMENTS:\n\n" +
                        "• Essential for removing ammonia, moisture, and heat\n" +
                        "• Windows should be at least 20% of floor area\n" +
                        "• Use wire mesh on openings to keep out predators\n" +
                        "• Position windows/vents on opposite walls\n" +
                        "• Consider roof vents for hot climates\n" +
                        "• Avoid direct drafts on birds\n" +
                "• Adjust ventilation based on season and bird age\n"+
                "• In hot weather, maximize airflow\n"+
                "• In cold weather, reduce openings while maintaining air quality");

        housingInfo.put("lighting", "LIGHTING REQUIREMENTS:\n\n"
                + "• Layers need 16-17 hours of light daily for optimal production\n"
                + "• Broilers benefit from 23 hours light, 1 hour dark\n"
                + "• Natural light can be supplemented with artificial lighting\n"
                + "• Use 1 watt per 3-4 square feet of floor space\n"
                + "• Position lights to evenly illuminate the house\n"
                + "• Clean bulbs regularly to maintain intensity\n"
                + "• Consider using energy-efficient LED bulbs\n"
                + "• Light intensity affects bird activity and feed consumption");

        housingInfo.put("systems", "HOUSING SYSTEMS:\n\n"
                + "1. Deep Litter System:\n"
                + "• Most common in Kenya\n"
                + "• Floor covered with 4-6 inches of litter material\n"
                + "• Space requirements: 2-3 sq ft per layer, 1-1.5 sq ft per broiler\n"
                + "• Good for all breeds\n"
                + "• Litter can be composted after use\n\n"
                + "2. Battery Cage System:\n"
                + "• Efficient for commercial layers\n"
                + "• Higher initial investment\n"
                + "• Better disease control\n"
                + "• Easier egg collection and manure management\n"
                + "• Space: 0.5-0.6 sq ft per bird\n\n"
                + "3. Slatted Floor System:\n"
                + "• Elevated floor with slats/wire mesh\n"
                + "• Manure drops through to collection area\n"
                + "• Good ventilation and cleanliness\n"
                + "• Higher construction cost\n\n"
                + "4. Free-Range System:\n"
                + "• Birds have access to outdoor areas\n"
                + "• Lower stocking density\n"
                + "• Better for indigenous breeds\n"
                + "• Lower feed costs (foraging)\n"
                + "• Higher predation risk");

        housingInfo.put("equipment", "ESSENTIAL HOUSING EQUIPMENT:\n\n"
                + "1. Feeders:\n"
                + "• Provide 4 inches of feeder space per adult bird\n"
                + "• Position at back height to reduce wastage\n"
                + "• Types: trough feeders, tube feeders, automatic chain feeders\n\n"
                + "2. Waterers:\n"
                + "• Ensure clean, fresh water always available\n"
                + "• Provide 1 inch of water space per bird\n"
                + "• Types: bell drinkers, nipple drinkers, trough waterers\n"
                + "• Position to minimize spillage onto litter\n\n"
                + "3. Nests (for layers):\n"
                + "• Provide 1 nest box for every 4-5 hens\n"
                + "• Size: 12x12x12 inches per nest\n"
                + "• Place in darker, quieter area of house\n"
                + "• Line with clean nesting material\n\n"
                + "4. Perches/Roosts:\n"
                + "• Provide 8-10 inches of perch space per bird\n"
                + "• Position 2 feet above floor\n"
                + "• Use 2-inch diameter rounded wood\n\n"
                + "5. Brooder (for chicks):\n"
                + "• Heat source maintaining 95°F (35°C) for first week\n"
                + "• Reduce temperature by 5°F weekly until ambient\n"
                + "• Chick guard to keep chicks near heat source");
    }

    private void initializeFeedingInfo() {
        feedingInfo = new HashMap<>();

        feedingInfo.put("overview", "POULTRY FEEDING BASICS:\n\n"
                + "Proper nutrition is essential for health, growth, and productivity. Feed accounts for 60-70% of production costs.\n\n"
                + "Key nutrients needed:\n"
                + "• Protein: For growth, egg production, and tissue repair\n"
                + "• Energy: From carbohydrates and fats for body functions\n"
                + "• Vitamins & Minerals: For metabolism, bone development, and egg quality\n"
                + "• Water: Most critical nutrient, must be clean and fresh\n\n"
                + "What specific feeding information would you like to know?");

        feedingInfo.put("types", "TYPES OF POULTRY FEEDS:\n\n"
                + "1. Chick Starter (0-8 weeks):\n"
                + "• 20-22% protein content\n"
                + "• Finely ground for easy digestion\n"
                + "• Contains coccidiostat for disease prevention\n\n"
                + "2. Grower Feed (8-18 weeks):\n"
                + "• 16-18% protein content\n"
                + "• Prepares pullets for laying\n"
                + "• Lower in calcium than layer feed\n\n"
                + "3. Layer Feed (18+ weeks):\n"
                + "• 16-18% protein content\n"
                + "• High calcium (3-4%) for eggshell formation\n"
                + "• Balanced for sustained egg production\n\n"
                + "4. Broiler Starter (0-3 weeks):\n"
                + "• 22-24% protein content\n"
                + "• High energy for rapid growth\n\n"
                + "5. Broiler Finisher (3-6 weeks):\n"
                + "• 18-20% protein content\n"
                + "• Higher energy, lower protein than starter\n\n"
                + "6. Breeder Feed:\n"
                + "• Specially formulated for breeding flocks\n"
                + "• Balanced for fertility and hatchability");

        feedingInfo.put("frequency", "FEEDING FREQUENCY AND AMOUNTS:\n\n"
                + "Chicks (0-8 weeks):\n"
                + "• Feed available at all times (ad libitum)\n"
                + "• Consumption: 10-50g per day increasing with age\n"
                + "• Use shallow feeders for easy access\n\n"
                + "Growers (8-18 weeks):\n"
                + "• 2-3 times daily or ad libitum\n"
                + "• Consumption: 50-80g per day\n"
                + "• Control feed to prevent obesity in future layers\n\n"
                + "Layers (18+ weeks):\n"
                + "• 2-3 times daily or ad libitum\n"
                + "• Consumption: 100-120g per day\n"
                + "• Feed early morning and late afternoon for best results\n\n"
                + "Broilers:\n"
                + "• Ad libitum feeding for maximum growth\n"
                + "• Consumption increases from 30g to 180g daily by market age\n"
                + "• Ensure 24-hour access to feed for optimal growth");

        feedingInfo.put("homemade", "HOMEMADE FEED FORMULATIONS:\n\n"
                + "Basic Layer Feed Formula (100kg):\n"
                + "• Maize/corn: 55kg (energy source)\n"
                + "• Wheat bran: 10kg (fiber)\n"
                + "• Sunflower/cotton seed cake: 20kg (protein)\n"
                + "• Fish meal/omena: 10kg (protein)\n"
                + "• Limestone/crushed shells: 4kg (calcium)\n"
                + "• Premix (vitamins/minerals): 0.5kg\n"
                + "• Salt: 0.5kg\n\n"
                + "Basic Broiler Feed Formula (100kg):\n"
                + "• Maize/corn: 50kg (energy source)\n"
                + "• Wheat bran: 5kg (fiber)\n"
                + "• Sunflower/cotton seed cake: 20kg (protein)\n"
                + "• Fish meal/omena: 20kg (protein)\n"
                + "• Bone meal: 3kg (calcium/phosphorus)\n"
                + "• Premix (vitamins/minerals): 1kg\n"
                + "• Vegetable oil: 0.5kg (energy)\n"
                + "• Salt: 0.5kg\n\n"
                + "Always ensure ingredients are fresh, free from mold, and properly stored.");

        feedingInfo.put("water", "WATER REQUIREMENTS:\n\n"
                + "• Water is the most critical nutrient\n"
                + "• Birds consume approximately twice as much water as feed\n"
                + "• Clean, fresh water must be available at all times\n\n"
                + "Daily water consumption (approximate):\n"
                + "• Day-old chicks: 30-40ml per bird\n"
                + "• Growing chickens: 80-150ml per bird\n"
                + "• Layers: 200-250ml per bird\n"
                + "• Broilers (market age): 300-350ml per bird\n\n"
                + "Water consumption increases in hot weather by 50-100%\n\n"
                + "Water quality is crucial:\n"
                + "• pH should be 6.0-8.0\n"
                + "• Free from contaminants and pathogens\n"
                + "• Clean waterers daily\n"
                + "• Test water quality periodically if possible");

        feedingInfo.put("supplements", "FEED SUPPLEMENTS:\n\n"
                + "1. Vitamin-Mineral Premixes:\n"
                + "• Essential for metabolic functions\n"
                + "• Improves egg quality and hatchability\n"
                + "• Follow manufacturer recommendations for dosage\n\n"
                + "2. Calcium Supplements:\n"
                + "• Crushed limestone or oyster shells for layers\n"
                + "• Can be provided separately in a dish\n"
                + "• Critical for strong eggshells\n\n"
                + "3. Probiotics:\n"
                + "• Promotes beneficial gut bacteria\n"
                + "• Improves digestion and immunity\n"
                + "• Especially useful after antibiotic treatment\n\n"
                + "4. Enzymes:\n"
                + "• Improves digestibility of plant-based feeds\n"
                + "• Reduces feed costs by improving efficiency\n\n"
                + "5. Grit:\n"
                + "• Small stones/sand that aid digestion in the gizzard\n"
                + "• Essential for birds on whole grain diets\n"
                + "• Provide separately in a dish");
    }

    private void initializeDiseaseInfo() {
        diseaseInfo = new HashMap<>();

        diseaseInfo.put("overview", "POULTRY HEALTH MANAGEMENT:\n\n"
                + "Disease prevention is more effective than treatment. Watch for these general signs of illness:\n\n"
                + "• Reduced feed and water consumption\n"
                + "• Decreased egg production\n"
                + "• Huddling, lethargy, closed eyes\n"
                + "• Coughing, sneezing, nasal discharge\n"
                + "• Diarrhea (note color and consistency)\n"
                + "• Ruffled feathers, pale combs/wattles\n"
                + "• Lameness or neurological signs\n"
                + "• Sudden deaths\n\n"
                + "What specific disease information would you like to know?");

        diseaseInfo.put("newcastle", "NEWCASTLE DISEASE:\n\n"
                + "Cause: Paramyxovirus\n\n"
                + "Signs:\n"
                + "• Respiratory distress (gasping, coughing)\n"
                + "• Nervous symptoms (twisted neck, paralysis)\n"
                + "• Greenish diarrhea\n"
                + "• Drop in egg production\n"
                + "• High mortality (up to 100% in unvaccinated flocks)\n\n"
                + "Prevention:\n"
                + "• Vaccination at day 1, 14, 45 and every 3-4 months\n"
                + "• Biosecurity measures\n\n"
                + "Treatment:\n"
                + "• No specific treatment available\n"
                + "• Supportive care with antibiotics for secondary infections\n"
                + "• Infected birds should be isolated or culled");

        diseaseInfo.put("gumboro", "GUMBORO DISEASE (INFECTIOUS BURSAL DISEASE):\n\n"
                + "Cause: Birnavirus\n\n"
                + "Signs:\n"
                + "• Affects young birds (3-6 weeks)\n"
                + "• Whitish/watery diarrhea\n"
                + "• Depression, ruffled feathers\n"
                + "• Trembling, unsteady gait\n"
                + "• Pecking at vent area\n"
                + "• Moderate mortality (10-20%)\n\n"
                + "Prevention:\n"
                + "• Vaccination at day 7-14 and 21-28\n"
                + "• Good sanitation and biosecurity\n\n"
                + "Treatment:\n"
                + "• No specific treatment\n"
                + "• Provide vitamins and electrolytes in water\n"
                + "• Antibiotics only for secondary infections");

        diseaseInfo.put("mareks", "MAREK'S DISEASE:\n\n"
                + "Cause: Herpesvirus\n\n"
                + "Signs:\n"
                + "• Paralysis of legs, wings, neck\n"
                + "• Weight loss despite normal appetite\n"
                + "• Gray iris or irregular pupil\n"
                + "• Tumors in internal organs\n"
                + "• Affects birds 12-30 weeks old\n\n"
                + "Prevention:\n"
                + "• Vaccination at hatchery (day-old)\n"
                + "• Good sanitation and ventilation\n\n"
                + "Treatment:\n"
                + "• No treatment available\n"
                + "• Infected birds should be culled\n"
                + "• Disease is highly contagious");

        diseaseInfo.put("coccidiosis", "COCCIDIOSIS:\n\n"
                + "Cause: Eimeria protozoa\n\n"
                + "Signs:\n"
                + "• Bloody diarrhea\n"
                + "• Droopiness, ruffled feathers\n"
                + "• Pale combs and wattles\n"
                + "• Reduced feed consumption\n"
                + "• Dehydration\n\n"
                + "Prevention:\n"
                + "• Coccidiostats in feed\n"
                + "• Keep litter dry\n"
                + "• Good sanitation\n"
                + "• Avoid overcrowding\n\n"
                + "Treatment:\n"
                + "• Amprolium or sulfonamides in water\n"
                + "• Vitamin A and K supplements\n"
                + "• Electrolytes for dehydration");

        diseaseInfo.put("fowlpox", "FOWL POX:\n\n"
                + "Cause: Avipoxvirus\n\n"
                + "Signs:\n"
                + "• Two forms: Dry (skin) and Wet (mouth/respiratory)\n"
                + "• Dry form: Wart-like lesions on comb, wattles, face\n"
                + "• Wet form: Yellow patches in mouth, difficulty breathing\n"
                + "• Reduced egg production\n\n"
                + "Prevention:\n"
                + "• Vaccination at 6-10 weeks of age\n"
                + "• Control mosquitoes (vectors)\n\n"
                + "Treatment:\n"
                + "• No specific treatment\n"
                + "• Apply iodine to skin lesions\n"
                + "• Antibiotics for secondary infections\n"
                + "• Recovery takes 2-4 weeks");

        diseaseInfo.put("parasites", "EXTERNAL AND INTERNAL PARASITES:\n\n"
                + "External Parasites:\n\n"
                + "1. Mites:\n"
                + "• Signs: Restlessness, reduced production, anemia\n"
                + "• Treatment: Poultry-approved insecticides, dust baths\n\n"
                + "2. Lice:\n"
                + "• Signs: Irritation, feather damage, reduced production\n"
                + "• Treatment: Poultry-approved insecticides, dust baths\n\n"
                + "3. Fleas:\n"
                + "• Signs: Irritation, anemia in severe cases\n"
                + "• Treatment: Treat birds and environment\n\n"
                + "Internal Parasites:\n\n"
                + "1. Roundworms:\n"
                + "• Signs: Weight loss, diarrhea, weakness\n"
                + "• Treatment: Piperazine, levamisole, albendazole\n\n"
                + "2. Tapeworms:\n"
                + "• Signs: Weight loss despite good appetite\n"
                + "• Treatment: Praziquantel, niclosamide\n\n"
                + "3. Gapeworms:\n"
                + "• Signs: Gasping, head shaking, 'gaping'\n"
                + "• Treatment: Levamisole, fenbendazole\n\n"
                + "Regular deworming every 3 months is recommended");

        diseaseInfo.put("vaccination", "VACCINATION SCHEDULE FOR CHICKENS:\n\n"
                + "Layers/Dual Purpose:\n\n"
                + "• Day 1: Marek's Disease (at hatchery)\n"
                + "• Day 7: Newcastle Disease + Infectious Bronchitis\n"
                + "• Day 14: Gumboro Disease\n"
                + "• Day 21: Newcastle Disease + Infectious Bronchitis (booster)\n"
                + "• Day 28: Gumboro Disease (booster)\n"
                + "• Week 8: Fowl Pox\n"
                + "• Week 12: Fowl Typhoid\n"
                + "• Week 16: Newcastle Disease + Infectious Bronchitis\n"
                + "• Every 3-4 months: Newcastle Disease booster\n\n"
                + "Broilers:\n\n"
                + "• Day 1: Marek's Disease (at hatchery)\n"
                + "• Day 7: Newcastle Disease + Infectious Bronchitis\n"
                + "• Day 14: Gumboro Disease\n"
                + "• Day 21: Newcastle Disease (booster)\n\n"
                + "Note: This is a general schedule. Consult a local vet for region-specific recommendations.");
    }

    private void initializeBusinessInfo() {
        businessInfo = new HashMap<>();

        businessInfo.put("overview", "POULTRY BUSINESS BASICS:\n\n"
                + "Poultry farming can be profitable with proper planning and management. Key business aspects include:\n\n"
                + "• Market research and business planning\n"
                + "• Start-up and operational costs\n"
                + "• Marketing and sales strategies\n"
                + "• Record keeping and financial management\n"
                + "• Value addition opportunities\n\n"
                + "What specific business information would you like to know?");

        businessInfo.put("planning", "BUSINESS PLANNING:\n\n"
                + "Before starting a poultry business:\n\n"
                + "1. Market Research:\n"
                + "• Identify target customers (households, restaurants, institutions)\n"
                + "• Study local demand for eggs, meat, or live birds\n"
                + "• Analyze competition and pricing\n"
                + "• Identify seasonal variations in demand\n\n"
                + "2. Business Plan Components:\n"
                + "• Executive summary\n"
                + "• Business description and objectives\n"
                + "• Production plan (breed, housing, feeding)\n"
                + "• Marketing strategy\n"
                + "• Financial projections (start-up costs, operating expenses, revenue)\n"
                + "• Risk assessment and mitigation\n\n"
                + "3. Legal Requirements:\n"
                + "• Business registration\n"
                + "• County permits\n"
                + "• Health certificates\n"
                + "• Environmental compliance");

        businessInfo.put("costs", "COSTS AND FINANCIAL PLANNING:\n\n"
                + "Start-up Costs (for 100 birds):\n\n"
                + "• Housing construction: KSh 50,000-150,000\n"
                + "• Equipment (feeders, waterers): KSh 15,000-30,000\n"
                + "• Initial stock (day-old chicks): KSh 10,000-15,000\n"
                + "• Initial feed (first month): KSh 15,000-20,000\n"
                + "• Vaccines and medications: KSh 5,000-10,000\n\n"
                + "Operating Costs (monthly for 100 layers):\n\n"
                + "• Feed: KSh 30,000-40,000\n"
                + "• Labor: KSh 5,000-15,000\n"
                + "• Utilities: KSh 2,000-5,000\n"
                + "• Medications/vaccines: KSh 2,000-5,000\n"
                + "• Miscellaneous: KSh 3,000-5,000\n\n"
                + "Expected Returns (monthly for 100 layers):\n\n"
                + "• Egg production: 80-90 eggs/day (2,400-2,700/month)\n"
                + "• Revenue at KSh 15 per egg: KSh 36,000-40,500\n"
                + "• Spent hens (after 18 months): KSh 500-700 each\n\n"
                + "Break-even typically occurs after 6-8 months for layers");

        businessInfo.put("marketing", "MARKETING STRATEGIES:\n\n"
                + "1. Product Strategies:\n"
                + "• Ensure consistent quality and supply\n"
                + "• Consider packaging for eggs (branded cartons)\n"
                + "• Value addition (sorted eggs, dressed chicken)\n"
                + "• Organic/free-range certification if applicable\n\n"
                + "2. Pricing Strategies:\n"
                + "• Cost-plus pricing (production cost + profit margin)\n"
                + "• Competitive pricing (market rate)\n"
                + "• Volume discounts for wholesale buyers\n"
                + "• Premium pricing for specialty products (organic, indigenous)\n\n"
                + "3. Distribution Channels:\n"
                + "• Direct to consumers (farm gate sales)\n"
                + "• Local markets and shops\n"
                + "• Hotels, restaurants, institutions\n"
                + "• Wholesalers and aggregators\n"
                + "• Online platforms and social media\n\n"
                + "4. Promotion:\n"
                + "• Word of mouth and referrals\n"
                + "• Social media marketing\n"
                + "• Local agricultural shows\n"
                + "• Signage and business cards\n"
                + "• Relationship building with regular customers");

        businessInfo.put("records", "RECORD KEEPING:\n\n"
                + "Essential Records to Maintain:\n\n"
                + "1. Production Records:\n"
                + "• Daily egg collection\n"
                + "• Mortality rates\n"
                + "• Growth rates (for broilers)\n"
                + "• Feed consumption\n"
                + "• Water consumption\n\n"
                + "2. Financial Records:\n"
                + "• Expenses (itemized)\n"
                + "• Sales and revenue\n"
                + "• Profit and loss statements\n"
                + "• Cash flow\n"
                + "• Assets and liabilities\n\n"
                + "3. Health Records:\n"
                + "• Vaccination schedule and dates\n"
                + "• Disease occurrences and treatments\n"
                + "• Medication usage\n\n"
                + "4. Inventory Records:\n"
                + "• Feed stock\n"
                + "• Medication stock\n"
                + "• Equipment\n\n"
                + "Good record keeping helps identify problems early, track profitability, and make informed business decisions.");

        businessInfo.put("value", "VALUE ADDITION OPPORTUNITIES:\n\n"
                + "Increase profitability through value addition:\n\n"
                + "1. Egg Products:\n"
                + "• Graded and sorted eggs (size, color)\n"
                + "• Branded packaging\n"
                + "• Boiled eggs for local markets\n"
                + "• Liquid egg products\n\n"
                + "2. Meat Products:\n"
                + "• Dressed chicken (whole)\n"
                + "• Cut pieces (breasts, wings, thighs)\n"
                + "• Marinated products\n"
                + "• Smoked or dried chicken\n\n"
                + "3. By-products:\n"
                + "• Manure as organic fertilizer\n"
                + "• Composted litter for gardening\n"
                + "• Feathers for crafts or pillows\n\n"
                + "4. Services:\n"
                + "• Hatchery services\n"
                + "• Training for new farmers\n"
                + "• Farm tours for schools\n\n"
                + "5. Integration:\n"
                + "• Feed production\n"
                + "• Crop farming using poultry manure\n"
                + "• Small-scale processing facility");
    }

    private void initializeBiosecurityInfo() {
        biosecurityInfo = new HashMap<>();

        biosecurityInfo.put("overview", "BIOSECURITY MEASURES:\n\n"
                + "Biosecurity refers to practices designed to prevent the introduction and spread of diseases. Implementing good biosecurity is one of the most cost-effective ways to maintain flock health.\n\n"
                + "Key biosecurity areas include:\n"
                + "• Farm location and design\n"
                + "• Access control\n"
                + "• Sanitation procedures\n"
                + "• Bird management\n"
                + "• Feed and water safety\n"
                + "• Waste management\n"
                + "• Health monitoring\n\n"
                + "What specific biosecurity information would you like to know?");

        biosecurityInfo.put("access", "ACCESS CONTROL MEASURES:\n\n"
                + "1. Farm Perimeter Security:\n"
                + "• Fence the farm area\n"
                + "• Post 'No Entry' signs\n"
                + "• Lock gates when possible\n"
                + "• Create a single entry point\n\n"
                + "2. Visitor Protocols:\n"
                + "• Maintain a visitor log book\n"
                + "• Restrict unnecessary visitors\n"
                + "• No contact with other poultry farms same day\n"
                + "• Provide farm-specific footwear and clothing\n\n"
                + "3. Vehicle Control:\n"
                + "• Disinfect vehicles entering farm\n"
                + "• Designated parking away from poultry houses\n"
                + "• Delivery vehicles should not enter production areas\n\n"
                + "4. Staff Practices:\n"
                + "• Dedicated work clothes and boots\n"
                + "• Hand washing facilities at entry points\n"
                + "• No keeping birds at home\n"
                + "• Work flow from clean to dirty areas");

        biosecurityInfo.put("sanitation", "CLEANING AND DISINFECTION:\n\n"
                + "1. Routine Cleaning:\n"
                + "• Daily removal of dead birds\n"
                + "• Regular cleaning of feeders and waterers\n"
                + "• Maintain dry litter\n"
                + "• Remove spilled feed promptly\n\n"
                + "2. Between Flocks (All-In-All-Out):\n"
                + "• Remove all litter and manure\n"
                + "• Dry clean to remove organic matter\n"
                + "• Wash with detergent and water\n"
                + "• Apply disinfectant after cleaning\n"
                + "• Allow downtime of 1-2 weeks between flocks\n\n"
                + "3. Effective Disinfectants:\n"
                + "• Phenolic compounds\n"
                + "• Quaternary ammonium compounds\n"
                + "• Sodium hypochlorite (bleach)\n"
                + "• Iodophors\n"
                + "• Glutaraldehyde\n\n"
                + "4. Footbaths:\n"
                + "• Place at entrance to each house\n"
                + "• Change solution daily or when visibly dirty\n"
                + "• Use appropriate disinfectant\n"
                + "• Clean boots of organic matter before dipping");

        biosecurityInfo.put("isolation", "ISOLATION AND SEPARATION:\n\n"
                + "1. New Bird Introduction:\n"
                + "• Quarantine new birds for 2-4 weeks\n"
                + "• Observe for signs of disease\n"
                + "• Introduce to flock only if healthy\n"
                + "• Purchase from reputable sources\n\n"
                + "2. Age Separation:\n"
                + "• Keep different age groups separate\n"
                + "• Maintain separate equipment for each age group\n"
                + "• Work with younger birds before older ones\n\n"
                + "3. Species Separation:\n"
                + "• Do not mix chickens with ducks, turkeys, etc.\n"
                + "• Different species can carry diseases without showing symptoms\n\n"
                + "4. Sick Bird Management:\n"
                + "• Isolate sick birds immediately\n"
                + "• Use separate equipment for sick pens\n"
                + "• Handle sick birds last\n"
                + "• Proper disposal of dead birds");

        biosecurityInfo.put("wildlife", "WILDLIFE AND PEST CONTROL:\n\n"
                + "1. Wild Bird Control:\n"
                + "• Bird-proof netting on windows and vents\n"
                + "• Repair holes in buildings\n"
                + "• Avoid attracting wild birds (clean up feed spills)\n"
                + "• Wild birds can transmit diseases like avian influenza\n\n"
                + "2. Rodent Control:\n"
                + "• Keep area around houses clear of debris\n"
                + "• Store feed in rodent-proof containers\n"
                + "• Use traps and approved rodenticides\n"
                + "• Seal entry points to buildings\n\n"
                + "3. Insect Control:\n"
                + "• Keep litter dry to prevent fly breeding\n"
                + "• Use approved insecticides when necessary\n"
                + "• Remove manure regularly\n"
                + "• Flies and beetles can transmit diseases\n\n"
                + "4. Predator Control:\n"
                + "• Secure housing against predators\n"
                + "• Proper fencing around free-range areas\n"
                + "• Night lighting to deter some predators\n"
                + "• Regular monitoring for predator activity");

        biosecurityInfo.put("feed", "FEED AND WATER BIOSECURITY:\n\n"
                + "1. Feed Storage:\n"
                + "• Store in clean, dry, rodent-proof containers\n"
                + "• Raise feed bags off floor on pallets\n"
                + "• First in, first out inventory management\n"
                + "• Discard moldy or contaminated feed\n\n"
                + "2. Feed Handling:\n"
                + "• Use clean equipment for feed distribution\n"
                + "• Prevent wild bird access to feeders\n"
                + "• Clean up spilled feed promptly\n"
                + "• Dedicated feed scoops for each house\n\n"
                + "3. Water Safety:\n"
                + "• Clean and sanitize water lines regularly\n"
                + "• Test water quality periodically\n"
                + "• Treat water if necessary (chlorination)\n"
                + "• Prevent standing water around houses\n"
                + "• Clean waterers daily");
    }

    private void initializeFarmingTips() {
        farmingTips = new String[] {
                "TIP: Add apple cider vinegar to drinking water (5ml per liter) once weekly to improve gut health and reduce pathogens.",

                "TIP: Sprinkle diatomaceous earth in dust bathing areas to help control external parasites naturally.",

                "TIP: Gradually introduce any feed changes over 7-10 days to prevent digestive upset and maintain production.",

                "TIP: During hot weather, provide cool water and feed during cooler parts of the day (early morning and evening).",

                "TIP: Collect eggs at least twice daily to maintain freshness and reduce breakage or contamination.",

                "TIP: Add crushed garlic to feed occasionally as a natural antibiotic and immune booster.",

                "TIP: Maintain a 16-hour light period for layers to optimize egg production.",

                "TIP: Keep detailed records of feed consumption, egg production, and mortality to track flock performance.",

                "TIP: Introduce new birds to the flock at night when existing birds are less territorial.",

                "TIP: Provide perches at different heights to allow natural roosting behavior and reduce stress.",

                "TIP: Rotate pasture areas for free-range chickens to prevent parasite buildup and allow vegetation recovery.",

                "TIP: Add herbs like oregano, thyme, and mint to chicken feed as natural antibiotics and to improve egg flavor.",

                "TIP: During vaccination, ensure all birds receive the proper dose by temporarily restricting water before administering vaccine in drinking water.",

                "TIP: Egg-eating behavior can be reduced by providing adequate calcium, collecting eggs frequently, and darkening nest boxes.",

                "TIP: Feather pecking can be minimized by reducing stocking density, providing adequate feeder space, and ensuring proper nutrition."
        };
    }

    public String getBreedInformation() {
        return breedInfo.get("overview");
    }

    public String getHousingInformation() {
        return housingInfo.get("overview");
    }

    public String getFeedingInformation() {
        return feedingInfo.get("overview");
    }

    public String getDiseaseInformation() {
        return diseaseInfo.get("overview");
    }

    public String getBusinessInformation() {
        return businessInfo.get("overview");
    }

    public String getBiosecurityInformation() {
        return biosecurityInfo.get("overview");
    }

    public String getRandomTip() {
        int index = random.nextInt(farmingTips.length);
        return farmingTips[index] + "\n\nWould you like more specific information about any aspect of poultry farming?";
    }
}