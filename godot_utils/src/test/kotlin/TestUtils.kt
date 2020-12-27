import java.io.InputStream
import java.net.URL

object TestUtils {

	/**
	 * Class loader object
	 */
	private val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()

	/**
	 * Get resource from this module
	 *
	 * @param location Target location to get
	 * @return [URL] Resource url
	 */
	fun getResource(location: String): URL? = classLoader.getResource(location)

	/**
	 * Get resource from this module
	 *
	 * @param location Target location to get
	 * @return [InputStream] Stream resource
	 */
	fun getResourceStream(location: String): InputStream? = classLoader.getResourceAsStream(location)
}
