package com.example.poyh

object WordRepository {

    private val categoryWords: Map<String, List<String>> = mapOf(
        "animals" to listOf(
            "Lion", "Elephant", "Giraffe", "Penguin", "Kangaroo",
            "Dolphin", "Cheetah", "Chimpanzee", "Crocodile", "Flamingo",
            "Hippopotamus", "Koala", "Octopus", "Panda", "Rhinoceros",
            "Tiger", "Zebra", "Gorilla", "Polar Bear", "Sloth",
            "Chameleon", "Bald Eagle", "Blue Whale", "Wolf", "Grizzly Bear",
            "Meerkat", "Ostrich", "Platypus", "Hedgehog", "Jaguar",
            "Walrus", "Seahorse"
        ),
        "movies" to listOf(
            "Titanic", "Avatar", "Inception", "The Lion King", "Jurassic Park",
            "The Matrix", "Harry Potter", "Star Wars", "The Avengers", "Gladiator",
            "Finding Nemo", "Forrest Gump", "Spider-Man", "Pirates of the Caribbean", "Back to the Future",
            "Frozen", "Shrek", "The Dark Knight", "Toy Story", "Interstellar",
            "The Lord of the Rings", "Pulp Fiction", "The Godfather", "Fight Club", "Casablanca",
            "Jaws", "Alien", "Terminator", "Indiana Jones", "Monsters Inc",
            "Up", "The Shawshank Redemption"
        ),
        "jobs" to listOf(
            "Astronaut", "Firefighter", "Surgeon", "Detective", "Pilot",
            "Chef", "Architect", "Lifeguard", "Archaeologist", "Veterinarian",
            "Photographer", "Dentist", "Electrician", "Journalist", "Plumber",
            "Software Engineer", "Scuba Diver", "Flight Attendant", "Magician", "Judge",
            "Paramedic", "Librarian", "Carpenter", "Baker", "Blacksmith",
            "Astronomer", "Geologist", "Stunt Performer", "Choreographer", "Beekeeper",
            "Sculptor", "Deep Sea Welder"
        ),
        "brands" to listOf(
            "Apple", "Nike", "Coca-Cola", "Google", "Samsung",
            "Microsoft", "McDonald's", "Mercedes-Benz", "Disney", "Amazon",
            "Adidas", "Ferrari", "BMW", "PlayStation", "Starbucks",
            "Netflix", "IKEA", "Gucci", "Tesla", "Lego",
            "Red Bull", "Sony", "Chanel", "Rolex", "Nintendo",
            "Pepsi", "Porsche", "Spotify", "Zara", "Louis Vuitton",
            "Ford", "Harley-Davidson"
        ),
        "singers" to listOf(
            "Michael Jackson", "Freddie Mercury", "Elvis Presley", "Beyonce", "Madonna",
            "Taylor Swift", "Eminem", "Ed Sheeran", "Adele", "Elton John",
            "Rihanna", "Bruno Mars", "Billie Eilish", "Bob Marley", "Lady Gaga",
            "David Bowie", "Katy Perry", "Justin Bieber", "Dua Lipa", "Drake",
            "Ariana Grande", "Paul McCartney", "Whitney Houston", "Shakira", "Stevie Wonder",
            "Celine Dion", "Post Malone", "Frank Sinatra", "The Weeknd", "Harry Styles",
            "Britney Spears", "Prince"
        ),
        "countries" to listOf(
            "Hungary", "United States", "Japan", "Brazil", "Australia",
            "Canada", "Germany", "France", "Italy", "United Kingdom",
            "Spain", "Egypt", "India", "South Africa", "Mexico",
            "Norway", "Greece", "Argentina", "New Zealand", "Thailand",
            "Switzerland", "South Korea", "Sweden", "Iceland", "Portugal",
            "Netherlands", "Turkey", "Ireland", "Morocco", "Vietnam",
            "Finland", "Austria"
        )
    )

    fun getWords(categoryId: String): List<String> {
        return categoryWords[categoryId.lowercase()] ?: categoryWords["animals"] ?: emptyList()
    }

    fun getShuffledWords(categoryId: String): List<String> {
        return getWords(categoryId).shuffled()
    }
}
