import 'package:equatable/equatable.dart';
import 'package:models/models.dart';
import 'package:repositories/repositories.dart';

class PageState extends Equatable {
  final List<PageLayout> items;
  final PageQuery query;
  final bool loading;
  final String error;
  final int page;
  final int pageSize;
  final int total;
  final FetchStatus status;

  const PageState({
    this.items = const [],
    this.query = const PageQuery(),
    this.loading = false,
    this.error = '',
    this.page = 0,
    this.pageSize = 10,
    this.total = 0,
    this.status = FetchStatus.initial,
  });

  PageState copyWith({
    List<PageLayout>? items,
    PageQuery? query,
    bool? loading,
    String? error,
    int? page,
    int? pageSize,
    int? total,
    FetchStatus? status,
  }) {
    return PageState(
      items: items ?? this.items,
      query: query ?? this.query,
      loading: loading ?? this.loading,
      error: error ?? this.error,
      page: page ?? this.page,
      pageSize: pageSize ?? this.pageSize,
      total: total ?? this.total,
      status: status ?? this.status,
    );
  }

  @override
  List<Object?> get props => [
        items,
        query,
        loading,
        error,
        page,
        pageSize,
        total,
        status,
      ];

  @override
  String toString() {
    return 'LayoutState(items: $items, query: $query, loading: $loading, error: $error, page: $page, pageSize: $pageSize, total: $total, status: $status)';
  }

  factory PageState.initial() {
    return const PageState();
  }
}
