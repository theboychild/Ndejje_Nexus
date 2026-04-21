package ug.ac.ndejje.nexus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ug.ac.ndejje.nexus.R
import ug.ac.ndejje.nexus.viewmodel.RegistrationState
import ug.ac.ndejje.nexus.viewmodel.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    onRegistrationSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var regNumber by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("") }
    var program by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val faculties = listOf(
        "Faculty of Business Administration & Management",
        "Faculty of Social Sciences and Arts",
        "Faculty of Science and Computing",
        "Faculty of Humanities and Education",
        "Faculty of Engineering and Survey",
        "Faculty of Environment and Agricultural Sciences"
    )

    val programsByLevelAndFaculty = mapOf(
        "Masters" to mapOf(
            "Faculty of Business Administration & Management" to listOf(
                "Master of Business Administration (MBA) – Accounting",
                "MBA – Management",
                "MBA – Marketing",
                "MBA – Oil and Gas Management",
                "MSc. in Accounting and Finance",
                "MSc. in Procurement and Supply Chain Management",
                "MSc. in Human Resource Management",
                "MSc. in Project Management",
                "Master of Economics"
            ),
            "Faculty of Social Sciences and Arts" to listOf(
                "MA in Public Administration and Management",
                "MA in Development Studies",
                "MA in Peace, Human Rights, and Development",
                "MA in Gender and Development",
                "MA in Journalism and Multimedia Studies",
                "MA in Counseling Psychology",
                "MA in Community Participation and Strategic Management",
                "Master of Public Relations Management"
            ),
            "Faculty of Science and Computing" to listOf(
                "Master of Computer Science (MCS)",
                "MSc. in Information Systems",
                "MSc. in Information Technology",
                "Master of Sports Science and Management",
                "Master of Science in Sports & Exercise Psychology",
                "MSc. in Statistics (Research Track)"
            ),
            "Faculty of Humanities and Education" to listOf(
                "Master of Education (MED)",
                "Master of Education in Administration, Planning, and Management",
                "Master of Education in Curriculum Studies",
                "Master of Educational Leadership",
                "MA in Christian Religious Studies"
            ),
            "Faculty of Engineering and Survey" to listOf(
                "MSc. in Construction and Project Management",
                "MSc. in Civil Engineering (Specialized Track)",
                "MSc. in Electrical Engineering (Specialized Track)",
                "MSc. in Mechanical Engineering (Specialized Track)",
                "MSc. in Surveying and Land Studies"
            ),
            "Faculty of Environment and Agricultural Sciences" to listOf(
                "Master of Sustainable Agriculture and Rural Development (MSARD)",
                "Master of Environmental and Natural Resources Management",
                "Master of Agribusiness Management",
                "MSc. in Integrated Watershed Management",
                "MSc. in Disaster Risk Management",
                "MSc. in Climate Change and Development",
                "MSc. in Forestry and Nature Conservation"
            )
        ),
        "Postgraduate Diploma (PGD)" to mapOf(
            "Faculty of Business Administration & Management" to listOf(
                "PGD in Monitoring and Evaluation",
                "PGD in Human Resource Management",
                "PGD in Oil and Gas Management"
            ),
            "Faculty of Social Sciences and Arts" to listOf(
                "PGD in Guidance and Counseling",
                "PGD in Public Administration",
                "PGD in Development Studies",
                "PGD in Community Participation"
            ),
            "Faculty of Science and Computing" to listOf(
                "PGD in Computer Science",
                "PGD in Information Systems",
                "PGD in Information Technology",
                "PGD in Sports Science",
                "PGD in Sports Nutrition and Management",
                "PGD in Physical Education & Sports Management"
            ),
            "Faculty of Humanities and Education" to listOf(
                "PGD in Education (Secondary)",
                "PGD in Pedagogy",
                "PGD in Early Childhood Education and Development",
                "PGD in Educational Institutional Management",
                "PGD in Higher Education Teaching",
                "PGD in Special Needs Education",
                "PGD in Educational Leadership and Management"
            ),
            "Faculty of Engineering and Survey" to listOf(
                "PGD in Construction and Project Management",
                "PGD in Electrical Engineering",
                "PGD in Civil Engineering",
                "PGD in Mechanical Engineering",
                "PGD in Surveying and Mapping",
                "PGD in Renewable Energy Systems",
                "PGD in Engineering Management"
            ),
            "Faculty of Environment and Agricultural Sciences" to listOf(
                "PGD in Environmental Management",
                "PGD in Sustainable Agriculture",
                "PGD in Agribusiness Management",
                "PGD in Disaster Risk Management",
                "PGD in Climate Change and Development"
            )
        ),
        "Undergraduate (Bachelors)" to mapOf(
            "Faculty of Business Administration & Management" to listOf("Bachelor of Business Administration", "Bachelor of Commerce", "Bachelor of Procurement"),
            "Faculty of Social Sciences and Arts" to listOf("Bachelor of Social Work", "Bachelor of Public Administration"),
            "Faculty of Science and Computing" to listOf("Bachelor of Computer Science", "Bachelor of Information Technology"),
            "Faculty of Engineering and Survey" to listOf("Bachelor of Civil Engineering", "Bachelor of Electrical Engineering")
        ),
        "Diploma" to mapOf(
            "Faculty of Business Administration & Management" to listOf("Diploma in Business Administration", "Diploma in Accounting"),
            "Faculty of Science and Computing" to listOf("Diploma in Computer Science", "Diploma in Information Technology"),
            "Faculty of Engineering and Survey" to listOf("Diploma in Civil Engineering")
        ),
        "Certificate" to mapOf(
            "Faculty of Business Administration & Management" to listOf("Certificate in Business Administration"),
            "Faculty of Science and Computing" to listOf("Certificate in Computer Science", "Certificate in Information Technology")
        )
    )

    val levels = listOf(
        "Certificate",
        "Diploma",
        "Undergraduate (Bachelors)",
        "Postgraduate Diploma (PGD)",
        "Masters",
        "PhD"
    )

    var facultyExpanded by remember { mutableStateOf(false) }
    var levelExpanded by remember { mutableStateOf(false) }
    var programExpanded by remember { mutableStateOf(false) }
    
    val registrationState by viewModel.registrationState.collectAsState()

    LaunchedEffect(registrationState) {
        if (registrationState is RegistrationState.Success) {
            onRegistrationSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.register_header)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name_hint)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = regNumber,
                onValueChange = { regNumber = it },
                label = { Text(stringResource(R.string.reg_number_hint)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Faculty Dropdown
            ExposedDropdownMenuBox(
                expanded = facultyExpanded,
                onExpandedChange = { facultyExpanded = !facultyExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = faculty,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.faculty_hint)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = facultyExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = facultyExpanded,
                    onDismissRequest = { facultyExpanded = false }
                ) {
                    faculties.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                faculty = selectionOption
                                facultyExpanded = false
                                program = "" // Reset program when faculty changes
                            }
                        )
                    }
                }
            }

            // Level Dropdown
            ExposedDropdownMenuBox(
                expanded = levelExpanded,
                onExpandedChange = { levelExpanded = !levelExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = level,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.level_hint)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = levelExpanded,
                    onDismissRequest = { levelExpanded = false }
                ) {
                    levels.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                level = selectionOption
                                levelExpanded = false
                                program = "" // Reset program when level changes
                            }
                        )
                    }
                }
            }

            // Program Dropdown (Dependent on Level and Faculty)
            ExposedDropdownMenuBox(
                expanded = programExpanded,
                onExpandedChange = { if (level.isNotEmpty() && faculty.isNotEmpty()) programExpanded = !programExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = program,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.program_hint)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = programExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    enabled = level.isNotEmpty() && faculty.isNotEmpty()
                )
                if (level.isNotEmpty() && faculty.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = programExpanded,
                        onDismissRequest = { programExpanded = false }
                    ) {
                        programsByLevelAndFaculty[level]?.get(faculty)?.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    program = selectionOption
                                    programExpanded = false
                                }
                            )
                        } ?: DropdownMenuItem(
                            text = { Text("No programs available for this selection") },
                            onClick = { programExpanded = false }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_hint)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.password_hint)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (registrationState is RegistrationState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.register(name, regNumber, faculty, level, program, email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(stringResource(R.string.register_button))
                }
            }

            if (registrationState is RegistrationState.Error) {
                Text(
                    text = (registrationState as RegistrationState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            TextButton(onClick = onNavigateBack) {
                Text("Already have an account? Login")
            }
        }
    }
}
