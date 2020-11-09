export function groupBy(list, keyGetter)
{
  const map = new Map();
  const listLength = list.length;

  for (let i = 0; i < listLength; i++)
  {
    const item = list[i];

    const key = keyGetter(item);
    const collection = map.get(key);

    !collection ? map.set(key, [item]) : collection.push(item)
  }

  return map;
}
