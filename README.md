WinSCP Password Recovery
========================

Decrypt stored WinSCP Passwords.

Having lost my FTP password, the need quickly arose to recover the password from WinSCP. Browsing some forums on how-to's, all I found were passwordbox-unmaskers, which I strongly dislike. One post from [anoopengineer](https://github.com/anoopengineer/) gave his Go implementation of the decryption: https://github.com/anoopengineer/winscppasswd

As there was no explanation whatsoever on where this information was coming from, and due to the lack of a Go compiler installed on my machine, I set out to implement the decryption algorithm myself, with help from the WinSCP source code (http://winscp.net/eng/download.php -- source).

The code provides references to the corresponding C++ parts, for others to see.
Start using: 

    java Main <hostname> <user> <encryptedstring>

where encryptedstring should be replaced with the registry located at:
[HKEY_CURRENT_USER\Software\Martin Prikryl\WinSCP 2\Sessions\SESSION NAME\Password] (thanks to [anoopengineer](https://github.com/anoopengineer/))
> Written with [StackEdit](https://stackedit.io/).