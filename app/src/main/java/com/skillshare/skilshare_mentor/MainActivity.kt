package com.skillshare.skilshare_mentor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.skillshare.skilshare_mentor.home.DashboardScreen
import com.skillshare.skilshare_mentor.home.DashboardTab
import com.skillshare.skilshare_mentor.login.LoginScreen
import com.skillshare.skilshare_mentor.profile.EditProfileScreen
import com.skillshare.skilshare_mentor.profile.entity.Teacher
import com.skillshare.skilshare_mentor.register.AcademicWorldScreen
import com.skillshare.skilshare_mentor.register.AllDoneScreen
import com.skillshare.skilshare_mentor.register.PersonalIdentityScreen
import com.skillshare.skilshare_mentor.register.PersonalInfoScreen
import com.skillshare.skilshare_mentor.register.UniversityDocumentScreen
import com.skillshare.skilshare_mentor.register.VerificationProfessionScreen
import com.skillshare.skilshare_mentor.register.WelcomeScreen
import com.skillshare.skilshare_mentor.ui.theme.SkillShareTheme
import com.skillshare.skilshare_mentor.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkillShareTheme {
                SkillShareApp()
            }
        }
    }
}

@Composable
fun SkillShareApp() {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Login) }

    when (currentScreen) {
        AppScreen.Login -> {
            LoginScreen(
                onLoginClick = { email, password ->
                    currentScreen = AppScreen.Dashboard
                },
                onSignUpClick = {
                    currentScreen = AppScreen.Welcome
                }
            )
        }
        AppScreen.Welcome -> {
            WelcomeScreen(
                onStartClick = {
                    currentScreen = AppScreen.PersonalInfo
                },
                onBackToLogin = {
                    currentScreen = AppScreen.Login
                }
            )
        }
        AppScreen.PersonalInfo -> {
            PersonalInfoScreen(
                onContinueClick = {
                    currentScreen = AppScreen.AcademicWorld
                },
                onBackClick = {
                    currentScreen = AppScreen.Welcome
                }
            )
        }
        AppScreen.AcademicWorld -> {
            AcademicWorldScreen(
                onContinueClick = {
                    currentScreen = AppScreen.VerificationProfession
                },
                onBackClick = {
                    currentScreen = AppScreen.PersonalInfo
                }
            )
        }
        AppScreen.VerificationProfession -> {
            VerificationProfessionScreen(
                onBackClick = {
                    currentScreen = AppScreen.AcademicWorld
                },
                onEmailVerified = {
                    currentScreen = AppScreen.PersonalIdentity
                },
                onDocumentSelected = {
                    currentScreen = AppScreen.UniversityDocument
                }
            )
        }
        AppScreen.UniversityDocument -> {
            UniversityDocumentScreen(
                onContinueClick = {
                    currentScreen = AppScreen.PersonalIdentity
                },
                onBackClick = {
                    currentScreen = AppScreen.VerificationProfession
                }
            )
        }
        AppScreen.PersonalIdentity -> {
            PersonalIdentityScreen(
                onContinueClick = {
                    currentScreen = AppScreen.AllDone
                },
                onBackClick = {
                    currentScreen = AppScreen.VerificationProfession
                }
            )
        }
        AppScreen.AllDone -> {
            AllDoneScreen(
                onGoHomeClick = {
                    currentScreen = AppScreen.Dashboard
                },
                onBackClick = {
                    currentScreen = AppScreen.PersonalIdentity
                }
            )
        }
        AppScreen.Dashboard -> {
            DashboardScreen(
                onSettingsClick = {
                    currentScreen = AppScreen.Settings
                },
                onLogout = {
                    currentScreen = AppScreen.Login
                },
                onEditProfile = {
                    currentScreen = AppScreen.EditProfile
                },
                initialTab = DashboardTab.Home
            )
        }
        AppScreen.EditProfile -> {
            val exampleTeacher = Teacher(
                firstName = "Sebastian",
                lastName = "Ramirez",
                nickname = "jh_slin",
                dateBirth = "1998-03-15",
                universityEmail = "sebastian.ramirez@upc.edu.pe",
                universityDocument = "U202345678",
                educationalCenter = "UPC",
                country = "Perú",
                gender = "Masculino",
                cover = "https://images5.alphacoders.com/107/1070324.jpg"
            )

            EditProfileScreen(
                teacher = exampleTeacher,
                onSave = { updatedTeacher ->
                    println("Perfil actualizado: $updatedTeacher")
                    currentScreen = AppScreen.Dashboard
                },
                onBack = {
                    currentScreen = AppScreen.Dashboard
                }
            )
        }

        AppScreen.Settings -> {
            SettingsScreen(
                onBackClick = {
                    currentScreen = AppScreen.Dashboard
                }
            )
        }
    }
}

sealed class AppScreen {
    object Login : AppScreen()
    object Welcome : AppScreen()
    object PersonalInfo : AppScreen()
    object AcademicWorld : AppScreen()
    object VerificationProfession: AppScreen()
    object PersonalIdentity: AppScreen()
    object AllDone: AppScreen()
    object UniversityDocument: AppScreen()
    object Dashboard: AppScreen()
    object EditProfile: AppScreen()
    object Settings: AppScreen()
}