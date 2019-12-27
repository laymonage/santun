package id.ac.ui.cs.mobileprogramming.sage.santun.util.data

class Compression {
    companion object {
        init {
            System.loadLibrary("native-lib")
        }

        external fun compress(input: String): String
    }
}
