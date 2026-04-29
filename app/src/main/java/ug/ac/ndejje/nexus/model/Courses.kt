package ug.ac.ndejje.nexus.model

object UniversityData {
    val levels = listOf(
        "Certificate",
        "Diploma",
        "Bachelor's Degree",
        "Postgraduate Diploma (PGD)",
        "Master's Degree",
        "Doctorate (PhD)"
    )

    val facultiesWithCourses = mapOf(
        "Faculty of Engineering" to listOf(
            "Civil Engineering",
            "Mechanical Engineering",
            "Electrical Engineering",
            "Mechatronics and Robotics",
            "Chemical Engineering",
            "Structural Engineering",
            "Telecommunications Engineering",
            "Aeronautical Engineering"
        ),
        "Faculty of Science" to listOf(
            "Biotechnology",
            "Microbiology",
            "Astrophysics",
            "Geology",
            "Statistics",
            "Industrial Chemistry",
            "Mathematics",
            "Molecular Biology"
        ),
        "Faculty of Health Sciences" to listOf(
            "Medicine and Surgery (MBChB/MBBS)",
            "Nursing Science",
            "Pharmacy",
            "Medical Laboratory Science",
            "Physiotherapy",
            "Radiography",
            "Public Health",
            "Dental Surgery"
        ),
        "Faculty of Arts and Humanities" to listOf(
            "English Literature",
            "Philosophy",
            "History and International Studies",
            "Linguistics",
            "Performing Arts (Music/Drama)",
            "Religious Studies",
            "Archaeology",
            "Visual Arts and Design"
        ),
        "Faculty of Social Sciences" to listOf(
            "Clinical Psychology",
            "Sociology",
            "International Relations",
            "Economics",
            "Political Science",
            "Mass Communication and Journalism",
            "Criminology and Security Studies",
            "Geography and Regional Planning"
        ),
        "Faculty of Business and Economics" to listOf(
            "Accounting",
            "Business Administration",
            "Finance and Investment",
            "Marketing",
            "Human Resource Management",
            "Procurement and Supply Chain Management",
            "Actuarial Science",
            "Entrepreneurship"
        ),
        "Faculty of Law" to listOf(
            "Commercial Law",
            "Criminal Law",
            "International Law",
            "Constitutional Law",
            "Environmental Law",
            "Human Rights Law",
            "Family Law",
            "Intellectual Property Law"
        ),
        "Faculty of Information Technology" to listOf(
            "Software Engineering",
            "Computer Science",
            "Cybersecurity",
            "Data Science",
            "Network Engineering",
            "Information Systems",
            "Artificial Intelligence",
            "Mobile Application Development"
        ),
        "Faculty of Education" to listOf(
            "Early Childhood Education",
            "Educational Psychology",
            "Guidance and Counseling",
            "Special Needs Education",
            "Science Education (Biology/Physics/Math)",
            "Physical and Health Education",
            "Adult Education",
            "Educational Management"
        ),
        "Faculty of Agriculture and Environmental Sciences" to listOf(
            "Crop Science",
            "Animal Science and Production",
            "Agricultural Economics",
            "Soil Science",
            "Forestry and Nature Conservation",
            "Food Science and Technology",
            "Horticulture",
            "Aquaculture and Fisheries Management"
        )
    )

    val faculties = facultiesWithCourses.keys.toList()
}
