package com.example.dragdroplinear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jmedeisis.draglinearlayout.DragLinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // below lines is to initialize our Drag linear layout
        DragLinearLayout dragLayout = findViewById(R.id.container);

        // we are creating for loop for dragging
        // and dropping of child items.
        for (int i=0; i<dragLayout.getChildCount(); i++) {
            View child = dragLayout.getChildAt(i);

            // below line will set all children draggable
            // except the header layout.
            // the child is its own drag handle.
            dragLayout.setViewDraggable(child, child);


        }

    }
}