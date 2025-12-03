package com.skillshare.skilshare_mentor

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skillshare.skilshare_mentor.home.DashboardScreen
import com.skillshare.skilshare_mentor.home.DashboardTab
import com.skillshare.skilshare_mentor.login.LoginScreen
import com.skillshare.skilshare_mentor.login.LoginViewModel
import com.skillshare.skilshare_mentor.profile.EditProfileScreen
import com.skillshare.skilshare_mentor.profile.EditProfileViewModel
import com.skillshare.skilshare_mentor.profile.entity.Teacher
import com.skillshare.skilshare_mentor.register.AcademicWorldScreen
import com.skillshare.skilshare_mentor.register.AllDoneScreen
import com.skillshare.skilshare_mentor.register.PersonalIdentityScreen
import com.skillshare.skilshare_mentor.register.PersonalInfoScreen
import com.skillshare.skilshare_mentor.register.RegisterViewModel
import com.skillshare.skilshare_mentor.register.UniversityDocumentScreen
import com.skillshare.skilshare_mentor.register.VerificationProfessionScreen
import com.skillshare.skilshare_mentor.register.WelcomeScreen
import com.skillshare.skilshare_mentor.settings.SettingsScreen
import com.skillshare.skilshare_mentor.ui.theme.AppTheme
import com.skillshare.skilshare_mentor.ui.theme.SkillShareTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentTheme by remember { mutableStateOf(AppTheme.System) }
            val useDarkTheme = when (currentTheme) {
                AppTheme.Light -> false
                AppTheme.Dark -> true
                AppTheme.System -> isSystemInDarkTheme()
            }

            var currentLanguageCode by remember {
                mutableStateOf(AppCompatDelegate.getApplicationLocales().toLanguageTags().ifEmpty { "en" })
            }

            SkillShareTheme(darkTheme = useDarkTheme) {
                SkillShareApp(
                    currentAppTheme = currentTheme,
                    onThemeChange = { newTheme -> currentTheme = newTheme },
                    currentLanguageCode = currentLanguageCode,
                    onLanguageChange = { newCode ->
                        currentLanguageCode = newCode
                        val appLocale = LocaleListCompat.forLanguageTags(newCode)
                        AppCompatDelegate.setApplicationLocales(appLocale)
                    }
                )
            }
        }
    }
}

@Composable
fun SkillShareApp(
    currentAppTheme: AppTheme = AppTheme.System,
    onThemeChange: (AppTheme) -> Unit = {},
    currentLanguageCode: String = "en",
    onLanguageChange: (String) -> Unit = {}
) {
    var currentScreenIndex by rememberSaveable { mutableIntStateOf(0) }

    var currentUserId by rememberSaveable { mutableIntStateOf(0) }
    var currentUserName by rememberSaveable { mutableStateOf("Docente") }
    var currentUserLastName by rememberSaveable { mutableStateOf("") }
    var currentInstitution by rememberSaveable { mutableStateOf("UPC") }

    // ViewModels
    val registerViewModel: RegisterViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val editProfileViewModel: EditProfileViewModel = viewModel()

    fun navigateTo(screen: AppScreen) {
        currentScreenIndex = getIndexFromScreen(screen)
    }

    val currentScreen = getScreenFromIndex(currentScreenIndex)

    when (currentScreen) {
        AppScreen.Login -> {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    currentUserId = loginViewModel.loggedInUserId ?: 0
                    currentUserName = loginViewModel.loggedInUserName
                    currentUserLastName = loginViewModel.loggedInLastName
                    currentInstitution = loginViewModel.loggedInInstitution

                    navigateTo(AppScreen.Dashboard)
                },
                onSignUpClick = {
                    navigateTo(AppScreen.Welcome)
                }
            )
        }
        AppScreen.Welcome -> {
            WelcomeScreen(
                onStartClick = { navigateTo(AppScreen.PersonalInfo) },
                onBackToLogin = { navigateTo(AppScreen.Login) }
            )
        }
        AppScreen.PersonalInfo -> {
            PersonalInfoScreen(
                viewModel = registerViewModel,
                onContinueClick = { navigateTo(AppScreen.AcademicWorld) },
                onBackClick = { navigateTo(AppScreen.Welcome) }
            )
        }
        AppScreen.AcademicWorld -> {
            AcademicWorldScreen(
                onContinueClick = { navigateTo(AppScreen.VerificationProfession) },
                onBackClick = { navigateTo(AppScreen.PersonalInfo) }
            )
        }
        AppScreen.VerificationProfession -> {
            VerificationProfessionScreen(
                viewModel = registerViewModel,
                onBackClick = { navigateTo(AppScreen.AcademicWorld) },
                onEmailVerified = { navigateTo(AppScreen.PersonalIdentity) },
                onDocumentSelected = { navigateTo(AppScreen.UniversityDocument) }
            )
        }
        AppScreen.UniversityDocument -> {
            UniversityDocumentScreen(
                onContinueClick = { navigateTo(AppScreen.PersonalIdentity) },
                onBackClick = { navigateTo(AppScreen.VerificationProfession) }
            )
        }
        AppScreen.PersonalIdentity -> {
            PersonalIdentityScreen(
                viewModel = registerViewModel,
                onContinueClick = { navigateTo(AppScreen.AllDone) },
                onBackClick = { navigateTo(AppScreen.VerificationProfession) }
            )
        }
        AppScreen.AllDone -> {
            AllDoneScreen(
                onGoHomeClick = { navigateTo(AppScreen.Dashboard) },
                onBackClick = { navigateTo(AppScreen.PersonalIdentity) }
            )
        }
        AppScreen.Dashboard -> {
            DashboardScreen(
                userName = currentUserName,
                userLastName = currentUserLastName,
                institution = currentInstitution,
                onSettingsClick = { navigateTo(AppScreen.Settings) },
                onLogout = { navigateTo(AppScreen.Login) },
                onEditProfile = { navigateTo(AppScreen.EditProfile) },
                initialTab = DashboardTab.Home
            )
        }
        AppScreen.EditProfile -> {
            val currentTeacherData = Teacher(
                firstName = currentUserName,
                lastName = currentUserLastName,
                educationalCenter = currentInstitution,
                nickname = "User",
                dateBirth = "",
                universityEmail = "",
                universityDocument = "",
                country = "Peru",
                gender = "Masculino",
                cover = "https://images5.alphacoders.com/107/1070324.jpg"
            )

            EditProfileScreen(
                viewModel = editProfileViewModel,
                userId = currentUserId,
                teacher = currentTeacherData,
                onSaveSuccess = { updatedTeacher ->
                    currentUserName = updatedTeacher.firstName
                    currentUserLastName = updatedTeacher.lastName
                    currentInstitution = updatedTeacher.educationalCenter

                    navigateTo(AppScreen.Dashboard)
                },
                onBack = { navigateTo(AppScreen.Dashboard) }
            )
        }
        AppScreen.Settings -> {
            SettingsScreen(
                onBackClick = { navigateTo(AppScreen.Dashboard) },
                currentTheme = currentAppTheme,
                onThemeChange = onThemeChange,
                currentLanguageCode = currentLanguageCode,
                onLanguageChange = onLanguageChange
            )
        }
    }
}

fun getIndexFromScreen(screen: AppScreen): Int {
    return when (screen) {
        AppScreen.Login -> 0
        AppScreen.Welcome -> 1
        AppScreen.PersonalInfo -> 2
        AppScreen.AcademicWorld -> 3
        AppScreen.VerificationProfession -> 4
        AppScreen.PersonalIdentity -> 5
        AppScreen.AllDone -> 6
        AppScreen.UniversityDocument -> 7
        AppScreen.Dashboard -> 8
        AppScreen.EditProfile -> 9
        AppScreen.Settings -> 10
    }
}

fun getScreenFromIndex(index: Int): AppScreen {
    return when (index) {
        0 -> AppScreen.Login
        1 -> AppScreen.Welcome
        2 -> AppScreen.PersonalInfo
        3 -> AppScreen.AcademicWorld
        4 -> AppScreen.VerificationProfession
        5 -> AppScreen.PersonalIdentity
        6 -> AppScreen.AllDone
        7 -> AppScreen.UniversityDocument
        8 -> AppScreen.Dashboard
        9 -> AppScreen.EditProfile
        10 -> AppScreen.Settings
        else -> AppScreen.Login
    }
}

sealed class AppScreen {
    object Login : AppScreen()
    object Welcome : AppScreen()
    object PersonalInfo : AppScreen()
    object AcademicWorld : AppScreen()
    object VerificationProfession : AppScreen()
    object PersonalIdentity : AppScreen()
    object AllDone : AppScreen()
    object UniversityDocument : AppScreen()
    object Dashboard : AppScreen()
    object EditProfile : AppScreen()
    object Settings : AppScreen()
}