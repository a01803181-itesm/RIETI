package itesm.rieti

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

data class SocialMediaLink(
    val platform: String,
    val accountName: String,
    val webUrl: String,
    val deepLinkUrl: String
)

class SipinnaUnitTest {

    private val sipinnaSocialLinks = listOf(
        SocialMediaLink(
            platform = "Instagram",
            accountName = "sipinnaatizapandezaragoza",
            webUrl = "https://www.instagram.com/sipinnaatizapandezaragoza",
            deepLinkUrl = "instagram://user?username=sipinnaatizapandezaragoza"
        ),
        SocialMediaLink(
            platform = "Facebook",
            accountName = "SIPINNAMX",
            webUrl = "https://www.facebook.com/SIPINNAMX",
            deepLinkUrl = "fb://facewebmodal/f?href=https://facebook.com/SIPINNAMX"
        ),
        SocialMediaLink(
            platform = "TikTok",
            accountName = "@sipinna_mx",
            webUrl = "https://www.tiktok.com/@sipinna_mx",
            deepLinkUrl = "https://www.tiktok.com/@sipinna_mx"
        )
    )

    @Test
    fun `redes sociales de SIPINNA contienen Instagram, Facebook y TikTok`() {
        assertEquals(3, sipinnaSocialLinks.size)
        val platforms = sipinnaSocialLinks.map { it.platform }
        assertTrue(platforms.contains("Instagram"))
        assertTrue(platforms.contains("Facebook"))
        assertTrue(platforms.contains("TikTok"))
    }

    @Test
    fun `URLs de redes sociales de SIPINNA son enlaces HTTPS validos`() {
        sipinnaSocialLinks.forEach { link ->
            assertTrue(
                "La URL de ${link.platform} debe comenzar con https://",
                link.webUrl.startsWith("https://")
            )
        }
    }

    @Test
    fun `nombres de usuario de redes sociales de SIPINNA estan formateados correctamente`() {
        val instagram = sipinnaSocialLinks.first { it.platform == "Instagram" }
        assertEquals("sipinnaatizapandezaragoza", instagram.accountName)

        val facebook = sipinnaSocialLinks.first { it.platform == "Facebook" }
        assertEquals("SIPINNAMX", facebook.accountName)

        val tiktok = sipinnaSocialLinks.first { it.platform == "TikTok" }
        assertEquals("@sipinna_mx", tiktok.accountName)
    }
}
