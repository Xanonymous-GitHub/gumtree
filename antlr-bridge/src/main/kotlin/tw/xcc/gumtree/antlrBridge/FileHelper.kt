package tw.xcc.gumtree.antlrBridge

import java.io.File

private fun File.isFileSafeToRead(): Boolean = this.exists() && this.isFile && this.canRead()

private fun File.isNotSymbolicLink(): Boolean = !this.toPath().toRealPath().equals(this.canonicalFile.toPath())

private fun File.isPathSecure(allowedDirectory: String): Boolean {
    val canonicalFilePath = this.canonicalPath
    val canonicalAllowedDirectory = File(allowedDirectory).canonicalPath
    return canonicalFilePath.startsWith(canonicalAllowedDirectory)
}

private fun File.isFileSizeAcceptable(maxSizeInBytes: Long): Boolean = this.length() <= maxSizeInBytes

private fun File.isFileTypeAllowed(allowedExtensions: Set<String>): Boolean {
    val fileExtension = this.extension
    return fileExtension in allowedExtensions
}

internal fun File.isValidToRead(
    maxSizeInBytes: Long,
    allowedDirectory: String,
    allowedExtensions: Set<String>
): Boolean =
    isFileSafeToRead() &&
        isPathSecure(allowedDirectory) &&
        isNotSymbolicLink() &&
        isFileSizeAcceptable(maxSizeInBytes) &&
        isFileTypeAllowed(allowedExtensions)