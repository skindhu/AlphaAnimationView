This is an Alpha in and out widget for Android

usage:   
AlphaAnimationView view  = new AlphaAnimationView(context);
view.setDuration(3000).setInterval(3000).setInterpolator(android.R.accelaterate_interpolator);
view.setAdapter(viewAdapter);
view.startAnimation();
