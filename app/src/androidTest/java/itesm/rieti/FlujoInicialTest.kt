package itesm.rieti

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import itesm.rieti.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlujoInicialTest
{
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun iniciaSesionYNavegaPorHaciaHistorial()
    {
        val correo = "casita@gmail.com"
        val contra = "123456"

        // Modificar los ID en todos
        onView(withId(R.id.editText))
            .perform(replaceText(correo), closeSoftKeyboard()) // Poner correo

        onView(withId(R.id.editText))
            .perform(replaceText(contra), closeSoftKeyboard()) // Poner contrase;a

        onView(withId(R.id.button))
            .perform(click()) // Click en registrarse

        onView(withId(R.id.button))
            .perform(click()) // Click a historial

        onView(withId(R.id.textView))
            .check(matches(withText(TEXTO))) // Forma de comprobar (modificar)
    }
}