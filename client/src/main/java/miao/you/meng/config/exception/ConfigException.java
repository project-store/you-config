package miao.you.meng.config.exception;

/**
 * Config root exception
 *
 * @author <a href="mailto:miaoyoumeng">Youmeng Miao </a>
 *
 */
public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfigException() {
		super();
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}

}
