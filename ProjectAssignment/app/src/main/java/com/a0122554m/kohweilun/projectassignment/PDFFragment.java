package com.a0122554m.kohweilun.projectassignment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This fragment has a big {@ImageView} that shows PDF pages, and 2
 * {@link Button}s to move between pages. We use a
 * {@link PdfRenderer} to render PDF pages as
 * {@link Bitmap}s.
 */
public class PDFFragment extends Fragment {

    public static final String PROGRESS_PREFS = "progress_state";
    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";
    private String FILENAME = "";
    private String TITLE = "";
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;
    private ImageView mImageView;
    private Button mButtonPrevious;
    private Button mButtonNext;
    private int mPageIndex;
    int furthestPage = 0;
    SharedPreferences sharedPreferences;

    public PDFFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        FILENAME = args.getString("fileName");
        TITLE = args.getString("title");
        sharedPreferences = getActivity().getSharedPreferences(PROGRESS_PREFS, Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.pdf_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Retain view references.
        mImageView = view.findViewById(R.id.image);
        mButtonPrevious = view.findViewById(R.id.previous);
        mButtonNext = view.findViewById(R.id.next);
        // Bind events.
        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPage(mCurrentPage.getIndex() - 1);
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPage(mCurrentPage.getIndex() + 1);
            }
        });

        mPageIndex = sharedPreferences.getInt(FILENAME + "_LASTSEEN", 0);
        furthestPage = sharedPreferences.getInt(FILENAME + "_FURTHEST", 0);

        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }

        AlertDialog.Builder ad_builder = new AlertDialog.Builder(this.getContext());
        ad_builder.setMessage("Which slide do you wish to return to?").setCancelable(false)
                .setPositiveButton("Last seen: " + (mPageIndex+1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton("Furthest: " + (furthestPage+1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showPage(furthestPage);
                        dialogInterface.cancel();
                    }
                });

        if (mPageIndex > 0 && furthestPage > 0) {
            AlertDialog alertDialog = ad_builder.create();
            alertDialog.setTitle("Options");
            alertDialog.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            openRenderer(getActivity());
            showPage(mPageIndex);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mCurrentPage) {
            outState.putInt(STATE_CURRENT_PAGE_INDEX, mCurrentPage.getIndex());
        }
    }

    /**
     * Sets up a {@link PdfRenderer} and related resources.
     */
    private void openRenderer(Context context) throws IOException {
        //PDF from the assets directory copied into cache directory.
        File file = new File(context.getCacheDir(), FILENAME);
        if (!file.exists()) {
            //PdfRenderer handle file from the cache directory.
            InputStream asset = context.getAssets().open(FILENAME);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        // This is the PdfRenderer we use to render the PDF.
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }
    }

    /**
     * Closes the {@link PdfRenderer} and related resources.
     *
     * @throws IOException When the PDF file cannot be closed.
     */
    private void closeRenderer() throws IOException {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        mPdfRenderer.close();
        mFileDescriptor.close();
    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * @param index The page index.
     */
    private void showPage(int index) {
        if (mPdfRenderer.getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }

        if (index > furthestPage) {
            furthestPage = index;
            updateFurthest(furthestPage);
            updateProgress(furthestPage);
//            Toast.makeText(getActivity(), "Furthest page: " + furthestPage, Toast.LENGTH_SHORT).show();
        }

        updateLastSeen(index);
//        Toast.makeText(getActivity(), "Last seen: " + (index + 1), Toast.LENGTH_SHORT).show();

        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        mImageView.setImageBitmap(bitmap);
        updateUi();
    }

    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    private void updateUi() {
        int index = mCurrentPage.getIndex();
        int displayIndex = index + 1;
        int pageCount = mPdfRenderer.getPageCount();
        mButtonPrevious.setEnabled(0 != index);
        mButtonNext.setEnabled(index + 1 < pageCount);
        getActivity().setTitle(TITLE + " (" + displayIndex + "/" + pageCount + ")");
    }

    /**
     * Gets the number of pages in the PDF. This method is marked as public for testing.
     *
     * @return The number of pages.
     */
    public int getPageCount() {
        return mPdfRenderer.getPageCount();
    }


    public void updateLastSeen(int currentPage) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FILENAME + "_LASTSEEN", currentPage);
        editor.commit();
    }

    public void updateFurthest(int furthestPage) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FILENAME + "_FURTHEST", furthestPage);
        editor.commit();
    }

    public void updateProgress(int furthestPage) {
        float percentage = (float) furthestPage / getPageCount() * 100;
        System.out.println(percentage);
        int progress = Math.round(percentage);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FILENAME + "_PROGRESS", progress);
        editor.commit();
    }
}
