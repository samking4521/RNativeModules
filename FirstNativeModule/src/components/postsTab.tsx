import { memo } from 'react';
import { View, Text } from 'react-native';

const PostsTab = memo(function PostsTab() {
  // Log once. The actual slowdown is inside SlowPost.
  console.log('[ARTIFICIALLY SLOW] Rendering 500 <SlowPost />');

  let items = [];
  for (let i = 0; i < 500; i++) {
    items.push(<SlowPost key={i} index={i} />);
  }
  return (
    <View>
      {items}
    </View>
  ); 
});

function SlowPost({ index }: {index: number}) {
  let startTime = performance.now();
  while (performance.now() - startTime < 1) {
    // Do nothing for 1 ms per item to emulate extremely slow code
  }

  return (
    <Text>
      Post #{index + 1}
    </Text>
  );
}

export default PostsTab;
