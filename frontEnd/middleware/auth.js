export default ({ redirect, store, route }) =>
{
  if(!store.state.user)
		return redirect('/')

	if(updateData(route.fullPath, store) != 0)
		return redirect('/not-authorized')
}

function updateData(path, store)
{
  const page = store.state.user.role.pages.filter(el => el.url === path)[0];  

  if(!page)
    return -1

  store.commit('setTitle', page.title);
  store.commit('setAuth', {
    create: page.create,
    modify: page.modify,
    delete: page.delete
  });

  return 0
}
