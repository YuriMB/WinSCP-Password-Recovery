import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuri Meiburg on 30-4-2015.
 */
public class Main {

    /**
     * ./core/Security.h:#define PWALG_SIMPLE_FLAG 0xFF
     */
    public static final int PWALG_SIMPLE_FLAG = 0xFF;

    /**
     * ./core/Security.h:#define PWALG_SIMPLE_MAGIC 0xA3
     */
    public static final char PWALG_SIMPLE_MAGIC = 0xA3;

    public static List<Character> fPassword = new ArrayList<Character>();
    public static String hostname, username;

    public static void main(String [] args){
        if (args.length != 3) {
            System.exit(0);
        }

        hostname = args[0];
        username = args[1];

        for( int i=0; i< args[2].length(); ++i){
            fPassword.add((char) Integer.parseInt(""+args[2].charAt(i),16));
        }

        System.out.println("username = " + username);
        System.out.println("hostname = " + hostname);
        System.out.println("getPassword() = " + getPassword());
    }


    /**
     * UnicodeString __fastcall TSessionData::GetPassword() const
     {
     return DecryptPassword(FPassword, UserName+HostName);
     }
     */
    static String getPassword(){
        return decryptPassword(fPassword, username + hostname);
    }

    /**
     * UnicodeString DecryptPassword(RawByteString Password, UnicodeString UnicodeKey, Integer)
     * {
     *    UTF8String Key = UnicodeKey;
     *    UTF8String Result("");
     *    Integer Index;
     *    unsigned char Length, Flag;
     *
     *    Flag = simpleDecryptNextChar(Password);
     *    if (Flag == PWALG_SIMPLE_FLAG)
     *    {
     *      simpleDecryptNextChar(Password);
     *      Length = simpleDecryptNextChar(Password);
     *    }
     *    else Length = Flag;
     *    Password.Delete(1, ((Integer)simpleDecryptNextChar(Password))*2);
     *    for (Index = 0; Index < Length; Index++)
     *        Result += (char)simpleDecryptNextChar(Password);
     *    if (Flag == PWALG_SIMPLE_FLAG)
     *    {
     *        if (Result.SubString(1, Key.Length()) != Key) Result = "";
     *        else Result.Delete(1, Key.Length());
     *    }
     *    return UnicodeString(Result);
     *}
     */
    static String decryptPassword(List<Character> password, String unicodeKey){
        System.out.println("unicodeKey = " + unicodeKey);
        String key = unicodeKey;
        String result = "";
        char length, flag;

        flag = simpleDecryptNextChar(password);
        System.out.println("flag = " + (int) flag);
        if(flag == PWALG_SIMPLE_FLAG){
            /* Dummy = */ simpleDecryptNextChar(password);
            length = simpleDecryptNextChar(password);
        }
        else length = flag;

        System.out.println("length = " + (int) length);

        int newStart = ((int)simpleDecryptNextChar(password)*2);
        System.out.println("newStart = " + newStart + ", password.size() = " + password.size());
        removeItems(password, 0, newStart);

        for(int index=0; index < length; ++index)
            result += simpleDecryptNextChar(password);

        System.out.println("result = " + result);
        if(flag == PWALG_SIMPLE_FLAG)
        {
            if (!result.substring(0, key.length()).equals(key)) result = "";
            else result = result.substring(key.length());
        }

        return result;
    }


    /**
     * unsigned char simpleDecryptNextChar(RawByteString &Str)
     {
     if (Str.Length() > 0)
     {
     unsigned char Result = (unsigned char)
     ~((((PWALG_SIMPLE_STRING.Pos(Str.c_str()[0])-1) << 4) +
     ((PWALG_SIMPLE_STRING.Pos(Str.c_str()[1])-1) << 0)) ^ PWALG_SIMPLE_MAGIC);
     Str.Delete(1, 2);
     return Result;
     }
     else return 0x00;
     }
     * @param str
     * @return
     */
    static public char simpleDecryptNextChar(List<Character> str){
        if(str.size() > 0){
            char result = unsignedChar(
                        ~(
                            (
                                    unsignedChar(str.get(0) << 4) + str.get(1) // Remove bitshift overflow bits.
                            ) ^ PWALG_SIMPLE_MAGIC
                        )
                    );

            removeItems(str, 0, 2);
            return result;
        }
        else return 0x00;
    }

    /**
     * Cut off anything over 255.
     * @param v
     * @return
     */
    static char unsignedChar(int v){
        return (char) (v & 0xFF);
    }

    /**
     * Remove items from list
     */
    static void removeItems(List lst, int start, int end){
        for(int i=0; i<end-start; ++i){
            lst.remove(start);
        }
    }
}
