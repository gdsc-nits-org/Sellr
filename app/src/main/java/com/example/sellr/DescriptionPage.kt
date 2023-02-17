package com.example.sellr

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sellr.databinding.ActivityDescrptionPageBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class DescriptionPage : AppCompatActivity() {
    private lateinit var binding: ActivityDescrptionPageBinding
    private lateinit var imageList: ArrayList<SlideModel>
    private lateinit var imageSlider: ImageSlider

    //fab idea
    //test
    private lateinit var backDrop: View
    private lateinit var lytMic: View
    private lateinit var lytCall: View
    private var rotate = false
    private var emailSeller = ""
    private var phoneSeller = ""
    private lateinit var expanded_image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescrptionPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            this.supportActionBar!!.hide()
        } // catch block to handle NullPointerException
        catch (_: NullPointerException) {
        }
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("key").toString()
            fetchDataFromDataBase(value)
        }
        expanded_image = binding.expandedImage
        imageList = ArrayList()
        imageSlider = findViewById(R.id.image_slider)



        backDrop = binding.backDrop
        lytMic = binding.lytMic
        lytCall = binding.lytCall

        val fabMic: FloatingActionButton = binding.fabMic
        val fabCall: FloatingActionButton = binding.fabCall
        val fabAdd: FloatingActionButton = binding.fabAdd

        initShowOut(lytMic)
        initShowOut(lytCall)

        backDrop.visibility = View.GONE

        fabAdd.setOnClickListener { v: View ->
            toggleFabMode(v)
        }

        backDrop.setOnClickListener {
            toggleFabMode(fabAdd)
        }

        lytMic.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$emailSeller")
            startActivity(intent)
        }

        lytCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneSeller")
            startActivity(intent)
        }
        fabMic.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$emailSeller")
            startActivity(intent)
        }

        fabCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneSeller")
            startActivity(intent)
        }
    }


    //For fab group display
    private fun toggleFabMode(v: View) {
        rotate = !rotate
        if (rotate) {
            showIn(lytMic)
            showIn(lytCall)
            backDrop.visibility = View.VISIBLE
        } else {
            showOut(lytMic)
            showOut(lytCall)
            backDrop.visibility = View.GONE
        }
    }

    private fun initShowOut(v: View) {
        v.visibility = View.GONE
        v.translationY = v.height.toFloat()
        v.alpha = 0f
    }

    private fun showIn(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 0f
        v.translationY = v.height.toFloat()
        v.animate()
            .setDuration(200)
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }
            })
            .alpha(1f)
            .start()
    }

    private fun showOut(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 1f
        v.translationY = 0f
        v.animate()
            .setDuration(200)
            .translationY(v.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    v.visibility = View.GONE
                    super.onAnimationEnd(animation)
                }
            }).alpha(0f)
            .start()
    }


    private fun fetchDataFromDataBase(items: String) {
        val databaseItem =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Items")
        databaseItem.child(items).get().addOnSuccessListener { dataSnapshot ->

            if (dataSnapshot.exists()) {
                val primaryImage = dataSnapshot.child("imagePrimary").value.toString()
                imageList.add(SlideModel(primaryImage))
                val imageArray = ArrayList<String>()

                imageArray.add(dataSnapshot.child("imageList").child("0").value.toString())
                imageArray.add(dataSnapshot.child("imageList").child("1").value.toString())
                imageArray.add(dataSnapshot.child("imageList").child("2").value.toString())
                for (i in imageArray.indices) {
                    if (imageArray[i] != "") {
                        imageList.add(SlideModel(imageArray[i]))
                    }
                }
                val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
                imageSlider.setImageList(imageList)
                imageSlider.setItemClickListener(object : ItemClickListener {
                    override fun onItemSelected(position: Int) {
                        expandImage(imageList[position].imageUrl.toString())
                    }
                })
                var sold = dataSnapshot.child("sold").value.toString()
                if (sold == "false") {
                    sold = "Available"
                    binding.status.text = sold
                } else {
                    sold = "Sold"
                    binding.status.text = sold
                }
                binding.productName.text = dataSnapshot.child("productName").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                binding.productCondition.text = dataSnapshot.child("condition").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                binding.descriptionHeading.text = dataSnapshot.child("productDesc").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                var category = dataSnapshot.child("category").value.toString()
                val addlCat = dataSnapshot.child("additionalCategory").value.toString()
                if (addlCat != "null") {
                    category = "$category($addlCat)"
                }
                binding.productCategory.text = category
                val date = "Selling Date: " + dataSnapshot.child("sellingDate").value.toString()
                binding.statusDate.text = date
                val price = "Rs. " + dataSnapshot.child("price").value.toString()
                binding.productPrice.text = price
                var useTime = dataSnapshot.child("usedForTime").value.toString()
                if (useTime == "") {
                    useTime = "-"
                }
                binding.productUsedFor.text = useTime
                val uid = dataSnapshot.child("userUID").value
                fillUser(uid)


            }
        }.addOnFailureListener {
            TODO("Not yet implemented")
        }

    }


    private fun fillUser(uid: Any?) {
        val databaseUser =
            FirebaseDatabase.getInstance("https://sellr-7a02b-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users")
        databaseUser.child(uid.toString()).get().addOnSuccessListener { snapshot ->

            if (snapshot.exists()) {
                val email = snapshot.child("email").value.toString()
                val phone = snapshot.child("phonenum").value.toString()
                val name = snapshot.child("name").value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                binding.sellerName.text = name
                binding.sellerEmail.text = email
                binding.sellerPhone.text = phone
                phoneSeller = phone
                emailSeller = email
            }
        }.addOnFailureListener {
            TODO("Not yet implemented")
        }
    }


    //WARNING
    //ANYTHING BEYOND THIS COMMENT IS PURE BLACK MAGIC, DON'T CHANGE ANYTHING OR THE SPELL WILL BREAK
    //COPY PASTED FROM THE DARK-HOLD OF ANDROID
    private fun expandImage(imageUrl: String) {
        val thumbView = binding.imageSlider
        zoomImageFromThumb(thumbView, imageUrl)
    }

    // Hold a reference to the current animator
    // so that it can be canceled midway.
    private var currentAnimator: Animator? = null

    // The system "short" animation time duration in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // frequently.
    private var shortAnimationDuration: Int = 400
    private fun zoomImageFromThumb(thumbView: View, imageUrl: String) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.
        Glide.with(expanded_image).load(imageUrl).placeholder(R.drawable.loading_desc)
            .into(expanded_image)
        //expanded_image.setImageResource(imageResId)
        binding.container.visibility = View.INVISIBLE
        // Calculate the starting and ending bounds for the zoomed-in image.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        binding.container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Using the "center crop" technique, adjust the start bounds to be
        // the same aspect ratio as the final bounds. This prevents
        // undesirable stretching during the animation. Calculate the
        // start scaling factor. The end scaling factor is always 1.0.
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally.
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically.
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it positions the zoomed-in view in the place of the
        // thumbnail.
        thumbView.alpha = 0f

        animateZoomToLargeImage(startBounds, finalBounds, startScale)

        setDismissLargeImageAnimation(thumbView, startBounds, startScale)
    }

    private fun animateZoomToLargeImage(startBounds: RectF, finalBounds: RectF, startScale: Float) {
        expanded_image.visibility = View.VISIBLE

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view. The default
        // is the center of the view.
        expanded_image.pivotX = 0f
        expanded_image.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties: X, Y, SCALE_X, and SCALE_Y.
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    expanded_image,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        expanded_image,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }
    }

    private fun setDismissLargeImageAnimation(
        thumbView: View,
        startBounds: RectF,
        startScale: Float
    ) {
        // When the zoomed-in image is tapped, it zooms down to the
        // original bounds and shows the thumbnail instead of
        // the expanded image.
        expanded_image.setOnClickListener {
            currentAnimator?.cancel()

            // Animate the four positioning and sizing properties in parallel,
            // back to their original values.
            currentAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(expanded_image, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(expanded_image, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(expanded_image, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        expanded_image.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        expanded_image.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
            binding.container.visibility = View.VISIBLE
        }
    }


}