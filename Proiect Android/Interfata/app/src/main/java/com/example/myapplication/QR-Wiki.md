ScanQR explained:

The class ScanQR is a Fragment linked to the <fragment> element in activity_main.xml.
This XML <fragment> element can be used in any other layout with cut/paste.
    <fragment
        android:id="@+id/scanner_fragment"
        android:name="**com.example.myapplication.ScanQR**" <= this is where the link is made
        android:layout_width="match_parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_height="300dp"
        android:layout_weight="1" />

AsyncQR inteface defines the callback onScanCompleted. MainActivity implements AsyncQR and handles
the scanned codes in onScanCompleted.

//https://stackoverflow.com/questions/16491284/how-to-make-a-callback-between-activity-and-fragment

