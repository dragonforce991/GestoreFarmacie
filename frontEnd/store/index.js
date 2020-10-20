const cookieparser = process.server ? require('cookieparser') : undefined

function findPage(id, arr)
{
	if(arr.length === 0)
		return false;

	return arr.reduce((a, item) => {
		if (a) return a;
		if (item.children && item.children.length > 0) return findPage(id, item.children);
		if (item.id === id) return item;
	}, null);
}

export const state = () => ({
  accessToken: null,
	authorizations: {},
  loggedIn: false,
	tree: [],
	pages: [],
	user: null,
	auth: null,
  title: 'Benvenuto'
})

export const mutations =
{
  setUser (state, user)
  {
    state.user = user;

    if(user)
    {
      for (let index = 0; index < user.role.pages.length; index++)
      {
        state.pages.push(user.role.pages[index]);
        const page = user.role.pages[index];

        state.authorizations[page.id] = {
          create: page.create,
          modify: page.modify,
          delete: page.delete,
        };

        const alreadyExists = !!findPage(page.id, state.tree);

        if(!page.parent && !alreadyExists)
        {
          state.tree.push(page);
        }
        else if(page.parent)
        {
          let parent = findPage(page.parent, state.tree);

          if(parent)
            parent.children = user.role.pages.filter(page => page.parent === parent.id);
        }
      }
    }
    else
    {
      state.authorizations = {};
      state.tree = []
    }
  },

	setTitle (state, data) {
    state.title = data
	},

  setLoggedIn (state, data) {
    state.loggedIn = data
  },

	setAuth (state, data) {
    state.auth = data
	}
}

export const actions =
{
	async nuxtServerInit ({ commit }, { req, redirect })
	{
    try
    {
      if(req.headers.cookie)
      {
        const { accessToken } = cookieparser.parse(req.headers.cookie);

        if(accessToken)
        {
          const user = await this.$axios.$get(`user/getUser`);

          commit('setUser', user);
          commit('setLoggedIn', true);
        }
        else
        {
          commit('setUser', null);
          commit('setLoggedIn', false);
          
          redirect('/')
        }
      }
    }
    catch (e)
    {
      console.error(e)
    }
  }
}
