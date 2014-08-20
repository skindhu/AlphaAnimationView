#An Carousel Widget with Alpha in and out effect for Android


##Usage:

```java
AlphaAnimationView view = new AlphaAnimationView(context);
view.setDuration(3000)
    .setInterval(3000)
    .setInterpolator(android.R.accelaterate_interpolator);
view.setAdapter(new MyViewAdapter());
view.startAnimation();


class MyViewAdapter extends ViewAdapter{
    @Override
    public int getCount() {}
    
    @Override
    public Object getItem(int position){}
    
    @Override
    public View getView(int position, View convertView) {}
}
```









