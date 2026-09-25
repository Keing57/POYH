package com.example.poyh

object WordRepository {

    private val categoryWords: Map<String, List<String>> = mapOf(
        "animals" to listOf(
            "Lion",
            "Elephant",
            "Giraffe",
            "Penguin",
            "Kangaroo",
            "Dolphin",
            "Cheetah",
            "Chimpanzee",
            "Crocodile",
            "Flamingo",
            "Hippopotamus",
            "Koala",
            "Octopus",
            "Panda",
            "Rhinoceros",
            "Tiger",
            "Zebra",
            "Gorilla",
            "Polar Bear",
            "Sloth"
        ),
        "movies" to listOf(
            "Titanic",
            "Avatar",
            "Inception",
            "The Lion King",
            "Jurassic Park",
            "The Matrix",
            "Harry Potter",
            "Star Wars",
            "The Avengers",
            "Gladiator",
            "Finding Nemo",
            "Forrest Gump",
            "Spider-Man",
            "Pirates of the Caribbean",
            "Back to the Future",
            "Frozen",
            "Shrek",
            "The Dark Knight",
            "Toy Story",
            "Interstellar"
        ),
        "jobs" to listOf(
            "Astronaut",
            "Firefighter",
            "Surgeon",
            "Detective",
            "Pilot",
            "Chef",
            "Architect",
            "Lifeguard",
            "Archaeologist",
            "Veterinarian",
            "Photographer",
            "Dentist",
            "Electrician",
            "Journalist",
            "Plumber",
            "Software Engineer",
            "Scuba Diver",
            "Flight Attendant",
            "Magician",
            "Judge"
        )
    )

    fun getWords(categoryId: String): List<String> {
        return categoryWords[categoryId.lowercase()] ?: categoryWords["animals"] ?: emptyList()
    }

    fun getShuffledWords(categoryId: String): List<String> {
        return getWords(categoryId).shuffled()
    }
}
