import com.saucelabs.saucerest.SauceREST
import geb.driver.SauceLabsDriverFactory
import org.openqa.selenium.firefox.FirefoxDriver

def sauceDriver = { def browserCaps ->
    def username = System.getenv("SAUCE_LABS_USER")
    assert username
    def accessKey = System.getenv("SAUCE_LABS_ACCESS_PASSWORD")
    assert accessKey
    def caps = [:]
    caps << browserCaps
    // FIXME: We don't need these, the geb-saucelabs plugin provides it
    /*
    caps.put('name', 'CukeBooks')
    caps.put('build', "git rev-parse HEAD".execute().text.trim())
    caps.put('tags', [
            System.getenv("CI_BRANCH") ?: "git symbolic-ref -q --short HEAD".execute().text.trim(),
            "git status --porcelain".execute().text ? 'dev' : ''
    ].findAll())
    */
    caps.put("selenium-version", "2.46.0")

    if (System.getProperty("grails.server.host")) {
        caps.put("initialBrowserUrl", "http://${System.getProperty("grails.server.host")}:${System.getProperty("grails.server.port", "8080")}/")
    }

    driver = {
        new SauceLabsDriverFactory().create(username, accessKey, caps)
    }
}

def sauceLabsBrowser = System.getProperty("geb.saucelabs.browser")
if (sauceLabsBrowser) {

    def browserCaps = new Properties()
    browserCaps.load(new StringReader(sauceLabsBrowser.replaceAll(',', '\n')))
    sauceDriver(browserCaps)
    sauceREST = new SauceREST(System.getenv("SAUCE_LABS_USER"), System.getenv("SAUCE_LABS_ACCESS_PASSWORD"))

} else {

    driver = {
        new FirefoxDriver()
    }

}
