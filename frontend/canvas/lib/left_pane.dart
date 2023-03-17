import 'package:common/common.dart';
import 'package:flutter/material.dart';

import '../../models/widget_data.dart';

class LeftPane extends StatelessWidget {
  const LeftPane({super.key, required this.widgets});
  final List<WidgetData> widgets;
  @override
  Widget build(BuildContext context) {
    return widgets
        .mapWithIndex((e, i) => _buildDraggableWidget(e, i))
        .toList()
        .toColumn()
        .scrollable();
  }

  Widget _buildDraggableWidget(WidgetData data, int index) {
    final listTile = ListTile(
      leading: Icon(
        data.icon,
        color: Colors.white54,
      ),
      title: Text(
        data.label,
        style: const TextStyle(color: Colors.white54),
      ),
    );
    return Draggable(
      data: data,
      feedback: listTile.card().opacity(0.5).constrained(
            width: 400,
            height: 80,
          ),
      child: listTile.card().constrained(
            height: 80,
          ),
    );
  }
}