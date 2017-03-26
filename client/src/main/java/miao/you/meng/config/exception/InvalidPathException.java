package miao.you.meng.config.exception;

/**
 * The exception that the file is invalid
 * 
 * @author <a href="mailto:miaoyoumeng">Youmeng Miao </a>
 *
 */
public class InvalidPathException extends ConfigException {

	private static final long serialVersionUID = 1L;

	public InvalidPathException() {
		super();
	}

	public InvalidPathException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPathException(String message) {
		super(message);
	}

	public InvalidPathException(Throwable cause) {
		super(cause);
	}

}
