part of 'page_block.dart';

class SliderPageBlock extends PageBlock {
  final List<ImageData> data;

  const SliderPageBlock({
    required int id,
    required String title,
    required int sort,
    required BlockConfig config,
    required this.data,
  }) : super(
          id: id,
          title: title,
          type: PageBlockType.slider,
          sort: sort,
          config: config,
        );

  @override
  List<Object?> get props => [id, type, sort, data, title];

  factory SliderPageBlock.fromJson(Map<String, dynamic> json) {
    return SliderPageBlock(
      id: json['id'],
      title: json['title'],
      sort: json['sort'],
      config: BlockConfig.fromJson(json['config']),
      data: (json['data'] as List).map((e) => ImageData.fromJson(e)).toList(),
    );
  }

  @override
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'type': describeEnum(type),
      'sort': sort,
      'config': config.toJson(),
      'data': data.map((e) => e.toJson()).toList(),
    };
  }

  SliderPageBlock copyWith({
    int? id,
    String? title,
    int? sort,
    BlockConfig? config,
    List<ImageData>? data,
  }) {
    return SliderPageBlock(
      id: id ?? this.id,
      title: title ?? this.title,
      sort: sort ?? this.sort,
      config: config ?? this.config,
      data: data ?? this.data,
    );
  }
}