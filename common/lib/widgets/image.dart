import 'package:flutter/material.dart';

class ImageWidget extends StatelessWidget {
  const ImageWidget({
    super.key,
    required this.imageUrl,
    required this.errorImage,
    this.fit = BoxFit.cover,
    this.height = double.infinity,
    this.width = double.infinity,
    this.alignment = Alignment.center,
  });
  final String imageUrl;
  final String errorImage;
  final BoxFit fit;
  final double height;
  final double width;
  final Alignment alignment;

  @override
  Widget build(BuildContext context) {
    return Image.network(
      imageUrl,
      fit: fit,
      height: height,
      width: width,
      alignment: alignment,
      loadingBuilder: (context, child, loadingProgress) {
        if (loadingProgress == null) return child;
        return Center(
          child: CircularProgressIndicator(
            value: loadingProgress.expectedTotalBytes != null
                ? loadingProgress.cumulativeBytesLoaded /
                    loadingProgress.expectedTotalBytes!
                : null,
          ),
        );
      },
      errorBuilder: (context, error, stackTrace) {
        return Image.asset(errorImage);
      },
    );
  }
}
